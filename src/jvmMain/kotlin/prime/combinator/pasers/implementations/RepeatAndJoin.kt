package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser

open class RepeatAndJoin<T, R : Parsed>(
    val parser: Parser<R>,
    val joinBetween: (parsed: List<R>) -> T
) : Parser<RepeatUntil<R, Not.NotParsed>.JoinedParsed<T>> {

    override fun parse(previous: Parsed): ParsedResult<RepeatUntil<R, Not.NotParsed>.JoinedParsed<T>> {
        return RepeatUntil(parser, Not(parser)).joinRepeaters {
            joinBetween(it)
        }.parse(previous)
    }
}




