package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.RepeatableBetween.RepeatableBetweenParsed
import java.util.*

/**
 * RepeatableBetween allows repeat parser included between 2 others.
 * Example:
 *  aim: we want parse all letters between braces, "[Na]"
 *  how to reach:   RepeatableBetween(Str("["), EnglishLetter(), Str("]")).parse("[Na]").get()
 *  result: successfully parsed letters "N","a" cause the located between braces.
 *  Parser has joinRepeaters which allows to join repeated parser and get in the example above string "Na" instead
 *  of set of letters. See TestParsers#testRepeatableBetweenJoined
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class RepeatableBetween<L : Parsed, M : Parsed, R : Parsed>(
    private val left: Parser<L>,
    private val between: Parser<M>,
    private val right: Parser<R>
) : Parser<RepeatableBetween<L, M, R>.RepeatableBetweenParsed> {

    inner class RepeatableBetweenParsed(
        val mappedFrom: Parsed,
        val left: L,
        val between: List<M>,
        val right: R
    ) : Parsed(mappedFrom)

    inner class RepeatableBetweenJoinedParsed<T>(
        val mapFrom: Parsed,
        val left: L,
        val between: T,
        val right: R
    ) : Parsed(mapFrom)

    override fun parse(previous: Parsed): ParsedResult<RepeatableBetweenParsed> {
        return Between(left, Repeat(between), right).map {
            RepeatableBetweenParsed(
                it,
                it.left,
                it.between.repeatersParsed,
                it.right
            )
        }.parse(previous)
    }

    fun <K> joinRepeaters(joinBetween: (parsed: List<M>) -> K): Parser<RepeatableBetweenJoinedParsed<K>> {
        return this.map {
            RepeatableBetweenJoinedParsed(it, it.left, joinBetween(it.between), it.right)
        }
    }
}
