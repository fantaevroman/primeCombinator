package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.AnyCharacter.AnyCharacterParsed
import java.util.*
import kotlin.Long

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