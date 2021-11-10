package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Beginning.BeginningParsed
import java.util.*
import kotlin.Long

/**
 * Beginning parser peeks beginning of the text, successful if we just started parse text
 * Example:
 *  aim: we want to make sure we start parsing from the beginning of the text
 *  how to reach: Beginning().parse("").get()
 *  result: successfully parsed because we started to parse empty string from the begninng
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class Beginning : Parser<BeginningParsed> {
    inner class BeginningParsed(previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<BeginningParsed> {

        return if (previous.indexEnd == -1L) {
            ParsedResult.asSuccess(BeginningParsed(previous, -1))
        } else {
            ParsedResult.asError("Not beginning of the text")
        }
    }
}
