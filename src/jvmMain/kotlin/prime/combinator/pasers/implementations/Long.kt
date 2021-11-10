package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Long.LongParsed
import java.util.*
import kotlin.Long


/**
 * Long allows parsing only long value
 * Example:
 *  aim: we want parse one long value
 *  how to reach:  Long().parse("134").get()
 *  result: successfully parsed long "134".
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class Long : EndOfInputParser<LongParsed>() {
    inner class LongParsed(val long: Long, text: String, indexStart: Long, indexEnd: Long) :
        Parsed(text, indexStart, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<LongParsed> {
        return Repeat(EnglishDigit())
            .joinRepeaters { it.map { it.digit.toString() }.joinToString(separator = "").toLong() }
            .map {
                LongParsed(it.joined, it.text, it.indexStart, it.indexEnd)
            }.parse(previous)
    }
}