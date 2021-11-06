package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.Repeat.RepeatParsed

open class Repeat<R : Parsed>(
    val parser: Parser<R>
) : Parser<Repeat<R>.RepeatParsed> {

    inner class RepeatParsed(
        previous: Parsed,
        val repeatersParsed: List<R>,
    ) : Parsed(
        previous,
        repeatersParsed.last().indexEnd
    )

    override fun parse(previous: Parsed): ParsedResult<RepeatParsed> {
        return buildParser().map {
            RepeatParsed(previous, it.repeatersParsed)
        }.parse(previous)
    }

    fun <K> joinRepeaters(joinBetween: (parsed: List<R>) -> K): Parser<RepeatUntil<R, Not.NotParsed>.JoinedParsed<K>> {
        return buildParser().joinRepeaters(joinBetween)
    }

    private fun buildParser() = RepeatUntil(parser, Not(parser))
}




