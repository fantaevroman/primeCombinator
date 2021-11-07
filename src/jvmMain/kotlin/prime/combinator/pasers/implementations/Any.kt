package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.Any.AnyParsed
import kotlin.Long

/**
 * Any parser allows to supply several Parsers and first successfull is peeked.
 * Example:
 *  aim: ee want to allow parse symbols "a", "b".
 *  how to reach: Any(Character('a'), Character('b')).parse(startParsing("b")).get()
 *  result: successfully parsed cause parser Character('b') was successful
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class Any(private vararg val parsers: Parser<*>) : EndOfInputParser<AnyParsed>() {
    inner class AnyParsed(previous: Parsed, val anyOne: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<AnyParsed> {
        val iterator = parsers.iterator()
        while (iterator.hasNext()) {
            val parserResult = iterator.next().parse(previous).map {
                AnyParsed(previous, it, it.indexEnd)
            }
            if (parserResult.success()) {
                return parserResult
            }
        }

        return ParsedResult.asError("No of supplied parsed matched at Any operation")
    }
}