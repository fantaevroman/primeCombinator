package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Character.CharacterParsed
import java.util.*
import kotlin.Long

/**
 * Character parser allows parsing specified character
 * Example:
 *  aim: we want to peek only specified character "a"
 *  how to reach:  Character('a').parse(startParsing("a")).get()
 *  result: successfully parsed because "a" because text contains "a"
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class Character(private val char: Char) : EndOfInputParser<CharacterParsed>() {
    inner class CharacterParsed(val char: Char, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<CharacterParsed> {
        val charParsed = previous.text.toCharArray()[previous.currentIndex().toInt()]
        return if (charParsed == char) {
            ParsedResult.asSuccess(CharacterParsed(charParsed, previous, previous.currentIndex()))

        } else {
            ParsedResult.asError("Can't parse character required:[$char] but was:[$charParsed]")
        }
    }
}
