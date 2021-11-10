package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.End.EndParsed
import kotlin.Long


/**
 * End allows specifying end of the text
 * Example:
 *  aim: we want parse end of text
 *  how to reach:  End().parse("").get()
 *  result: successfully parsed end of the text because it's empty and we reach end immediately
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class End() : Parser<EndParsed> {
    inner class EndParsed(previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<EndParsed> {
        return when {
            previous.indexEnd == -1L -> ParsedResult.asSuccess(
                EndParsed(
                    previous,
                    0
                )
            )

            previous.textMaxIndex().toLong() == previous.indexEnd -> ParsedResult.asSuccess(
                EndParsed(
                    previous,
                    previous.currentIndex()
                )
            )
            previous.textMaxIndex().toLong() < previous.indexEnd -> ParsedResult.asError("Not end")
            else -> ParsedResult.asError("End index is shorter than current position")
        }
    }
}
