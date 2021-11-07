package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.AnyCharacter.AnyCharacterParsed
import java.util.*
import kotlin.Long

/**
 * AnyCharacter parser peeks any character, digit or letter.
 * Example:
 *  aim: we want peek one any character
 *  how to reach: AnyCharacter().parse(startParsing("a")).get()
 *  result: successfully parsed cause allows any character including "a"
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class AnyCharacter : EndOfInputParser<AnyCharacterParsed>() {
    inner class AnyCharacterParsed(previous: Parsed, val char: Char, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<AnyCharacterParsed> {
        return ParsedResult.asSuccess(
            AnyCharacterParsed(
                previous,
                previous.text[previous.currentIndex().toInt()],
                previous.currentIndex()
            )
        )
    }
}