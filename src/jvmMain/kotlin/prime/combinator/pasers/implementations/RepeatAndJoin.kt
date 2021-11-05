package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.RepeatUntil.JoinedParsed

open class RepeatAndJoin<F, T>(
    val parser: Parser<out Parsed>,
    val joinBetween: (parsed: List<F>) -> T
) : Parser<JoinedParsed<T>> {

    override fun getType() = "RepeatAndJoin"

    override fun parse(parsed: Parsed): JoinedParsed<T> {
        return RepeatUntil(parser, Not(parser))
            .joinRepeaters { repeatersParsed: List<F> ->
                joinBetween(repeatersParsed)
            }.parse(parsed)
    }
}