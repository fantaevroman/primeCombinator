package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser


/**
 * EndOfInputParser is abstract and intended to be substructed by classes which counts for end of text.
 * If you inherit from EndOfInputParser class is automatically get end of text reached validation.
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
abstract class EndOfInputParser<T : Parsed> : Parser<T> {
    override fun parse(previous: Parsed): ParsedResult<T> {
        return if (previous.currentIndex() > previous.textMaxIndex()) {
            ParsedResult.asError("Unexpected end of input")
        } else {
            parseNext(previous)
        }
    }

    abstract fun parseNext(previous: Parsed): ParsedResult<T>
}