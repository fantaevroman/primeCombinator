package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Character.CharacterParsed
import java.util.*

class Character(private val char: Char) : EndOfInputParser<CharacterParsed>() {
    inner class CharacterParsed(
        val parsed: Parsed,
        val char: Char,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + 1,
            error.map { emptyMap<String, Char>() }.orElseGet { hashMapOf(Pair(getType(), char)) },
            getType(),
            error
        )

    override fun getType() = "Character"

    override fun parseNext(parsed: Parsed): CharacterParsed {
        val next = parsed.text.toCharArray()[parsed.currentIndex().toInt()]
        return if (next == char) {
            CharacterParsed(parsed, next)
        } else {
            asError(
                parsed,
                "Can't parse character at index:[${parsed.currentIndex()}], required:[$char] but was:[$next]"
            )
        }
    }

    override fun asError(parsed: Parsed, message: String): CharacterParsed {
        return CharacterParsed(parsed, 'e', Optional.of(ParsingError(message)))
    }
}
