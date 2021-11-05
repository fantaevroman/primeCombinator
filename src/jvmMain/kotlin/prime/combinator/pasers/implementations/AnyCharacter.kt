package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.AnyCharacter.AnyCharacterParsed
import java.util.*

class AnyCharacter : EndOfInputParser<AnyCharacterParsed>() {
    inner class AnyCharacterParsed(
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

    override fun getType() = "AnyCharacter"

    override fun parseNext(parsed: Parsed): AnyCharacterParsed {
        return AnyCharacterParsed(parsed, parsed.text[parsed.currentIndex().toInt()])
    }

    companion object {
        fun joinAsStr(
            list: List<Parsed>,
            separator: String = "",
            contextBodyName: String = "AnyCharacter"
        ): Parsed {
            return if (list.isEmpty()) {
                return Parsed("empty join Str", 0, 0, emptyMap(), "emptyJoin", Optional.empty())
            } else {
                val joinedStr = list.map { anyCharContext -> anyCharContext.context[contextBodyName] }
                    .joinToString(separator = separator)
                val lastCharParsed = list.last()
                return Str(joinedStr).StrParsed(lastCharParsed, joinedStr)
            }
        }
    }

    override fun asError(parsed: Parsed, message: String): AnyCharacterParsed {
        return AnyCharacterParsed(parsed, 'e', Optional.of(ParsingError(message)))
    }
}