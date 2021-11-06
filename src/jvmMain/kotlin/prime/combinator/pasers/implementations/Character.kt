package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Character.CharacterParsed
import java.util.*
import kotlin.Long

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
