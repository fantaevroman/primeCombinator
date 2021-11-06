package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import kotlin.Long

class RepeatUntil<R : Parsed, U : Parsed>(
    private val repeater: Parser<R>,
    private val until: Parser<U>
) : Parser<RepeatUntil<R, U>.RepeatUntilParsed> {

    inner class RepeatUntilParsed(
        val repeatersParsed: List<R>,
        val untilParsed: U,
    ) : Parsed(
        untilParsed.text,
        repeatersParsed.first().indexStart,
        untilParsed.indexEnd,
    )

    inner class JoinedParsed<T>(
        val joined: T,
        previous: Parsed,
        indexEnd: Long
    ) : Parsed(previous, indexEnd)


    override fun parse(previous: Parsed): ParsedResult<RepeatUntilParsed> {
        val repeaters = mutableListOf(repeater.parse(previous))
        val untils = mutableListOf<Parsed>()

        while (true) {
            if (!repeaters.last().success()) {
                return ParsedResult.asError(repeaters.last().error.get())
            }

            val currentUntil = until.parse(repeaters.last().get())
            if (currentUntil.success()) {
                untils.add(currentUntil.get())
                return ParsedResult.asSuccess(RepeatUntilParsed(repeaters.map { it.get() }, currentUntil.get()))
            }

            repeaters.add(repeater.parse(repeaters.last().get()))
        }
    }

    fun <K> joinRepeaters(joinBetween: (parsed: List<R>) -> K): Parser<JoinedParsed<K>> {
        return this.map {
            JoinedParsed(joinBetween(it.repeatersParsed), it.untilParsed, it.untilParsed.indexEnd)
        }
    }
}

