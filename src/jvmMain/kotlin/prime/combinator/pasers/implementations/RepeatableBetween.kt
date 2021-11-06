package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.RepeatableBetween.RepeatableBetweenParsed
import java.util.*

class RepeatableBetween<L : Parsed, M : Parsed, R : Parsed>(
    private val left: Parser<L>,
    private val between: Parser<M>,
    private val right: Parser<R>
) : Parser<RepeatableBetween<L, M, R>.RepeatableBetweenParsed> {

    inner class RepeatableBetweenParsed(
        val previous: Parsed,
        val left: L,
        val between: List<M>,
        val right: R
    ) : Parsed(previous, right.indexEnd)

    inner class RepeatableBetweenJoinedParsed<T>(
        val previous: Parsed,
        val left: L,
        val between: T,
        val right: R
    ) : Parsed(previous, right.indexEnd)

    override fun parse(previous: Parsed): ParsedResult<RepeatableBetweenParsed> {
        return Between(left, Repeat(between), right).map {
            RepeatableBetweenParsed(
                previous,
                it.left,
                it.between.repeatersParsed,
                it.right
            )
        }.parse(previous)
    }

    fun <K> joinRepeaters(joinBetween: (parsed: List<M>) -> K): Parser<RepeatableBetweenJoinedParsed<K>> {
        return this.map {
            RepeatableBetweenJoinedParsed(it.previous, it.left, joinBetween(it.between), it.right)
        }
    }
}
