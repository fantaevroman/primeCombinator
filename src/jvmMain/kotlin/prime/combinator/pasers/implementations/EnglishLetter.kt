package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.implementations.EnglishLetter.EnglishLetterParsed
import kotlin.Long

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
}