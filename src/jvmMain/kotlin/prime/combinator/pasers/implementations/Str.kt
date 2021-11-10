package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Str.StrParsed
import java.util.*
import kotlin.Long

/**
 * Str allows parsing specified string
 * Example:
 *  aim: we want to parse string "Name" in the beginning of text "Name is ..."
 *  how to reach: Str("Name").parse("Name is ...").get()
 *
 *  result: String "Name" is parsed.
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
open class Str(val str: String) : Parser<StrParsed> {
    inner class StrParsed(val str: String, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<StrParsed> {
        return if (previous.textMaxIndex() < previous.indexEnd + str.length) {
            ParsedResult.asError("Cant parse string, end of text")
        } else {
            val expectedIndex = previous.currentIndex().toInt()
            val indexOf = previous.text.indexOf(str, expectedIndex)
            return if (indexOf == expectedIndex) {
                ParsedResult.asSuccess(StrParsed(str, previous, previous.currentIndex() + str.length - 1))
            } else {
                ParsedResult.asError("Can't parse, [$str] not found")
            }
        }
    }
}