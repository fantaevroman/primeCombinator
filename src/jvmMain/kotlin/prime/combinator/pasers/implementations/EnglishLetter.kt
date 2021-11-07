package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.EnglishLetter.EnglishLetterParsed
import kotlin.Long


/**
 * EnglishLetter allows parsing only english letters (no special symbols)
 * Example:
 *  aim: we want parse one letter
 *  how to reach:  EnglishLetter().parse(startParsing("a")).get()
 *  result: successfully parsed letter "a".
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class EnglishLetter : EndOfInputParser<EnglishLetterParsed>() {
    inner class EnglishLetterParsed(val letter: Char, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<EnglishLetterParsed> {
        val char = previous.text.toCharArray()[previous.currentIndex().toInt()]
        return if (((char in 'a'..'z')) || ((char in 'A'..'Z'))) {
            ParsedResult.asSuccess(EnglishLetterParsed(char, previous, previous.currentIndex()))
        } else {
            ParsedResult.asError("[$char] not an english letter")
        }
    }

    fun asChar(): Parser<Character.CharacterParsed> {
         return this.map {
             Character(it.letter).CharacterParsed(it.letter, it.text, it.indexStart, it.indexEnd)
         }
    }
}