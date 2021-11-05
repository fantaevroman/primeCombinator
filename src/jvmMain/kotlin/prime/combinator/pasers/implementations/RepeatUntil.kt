package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.RepeatUntil.RepeatUntilParsed
import java.util.*
import kotlin.Any
import kotlin.Long

class RepeatUntil(
    private val repeater: Parser<out Parsed>,
    private val until: Parser<out Parsed>
) : Parser<RepeatUntilParsed> {

    inner class RepeatUntilParsed(
        val repeatersParsed: List<Parsed>,
        val untilParsed: Parsed,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            untilParsed.text,
            repeatersParsed.first().indexStart,
            untilParsed.indexEnd,
            error.map { emptyMap<String, Any>() }.orElseGet {
                hashMapOf(
                    Pair("repeaters", repeatersParsed),
                    Pair("until", untilParsed)
                )
            },
            getType(),
            error
        )

    inner class JoinedParsed<T>(
        val joined: Optional<T>,
        text: String,
        indexStart: Long,
        indexEnd: Long,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            text,
            indexStart,
            indexEnd,
            error.map { emptyMap<String, Any>() }.orElseGet {
                hashMapOf(
                    Pair("joined", joined as Any)
                )
            },
            getType(),
            error
        )

    override fun getType() = "RepeatUntil"

    override fun parse(parsed: Parsed): RepeatUntilParsed {
        val repeaters = mutableListOf(repeater.parse(parsed))
        val untils = mutableListOf<Parsed>()

        while (true) {
            if (repeaters.last().fail()) {
                return RepeatUntilParsed(repeaters, repeaters.last(), repeaters.last().error)
            }

            val currentUntil = until.parse(repeaters.last())
            if (currentUntil.success()) {
                untils.add(currentUntil)
                return RepeatUntilParsed(repeaters, currentUntil)
            }

            repeaters.add(repeater.parse(repeaters.last()))
        }
    }

    fun <T : Parsed, K> joinRepeaters(joinBetween: (parsed: List<T>) -> K): Parser<JoinedParsed<K>> {
        return this.map(
            { success ->
                JoinedParsed(
                    Optional.of(joinBetween(success.repeatersParsed as List<T>)),
                    success.text,
                    success.repeatersParsed.first().indexStart,
                    success.untilParsed.indexEnd,
                    success.error
                )
            },
            { fail ->
                val last = fail.repeatersParsed.last()
                JoinedParsed(
                    Optional.empty(),
                    last.text,
                    last.indexStart,
                    last.indexEnd,
                    fail.error
                )
            })
    }
}
