package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Character.CharacterParsed
import prime.combinator.pasers.implementations.CustomWord.CustomWordParsed
import java.util.*

class CustomWord(
    vararg val allowedChars: Parser<CharacterParsed>) : Parser<CustomWordParsed> {

    inner class CustomWordParsed(
        val parsed: Parsed,
        val customWord: String,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + customWord.length,
            error.map { emptyMap<String, String>() }.orElseGet { hashMapOf(Pair(getType(), customWord)) },
            getType(),
            error
        )

    override fun getType() = "CustomWord"

    override fun parse(parsed: Parsed): CustomWordParsed {
        val repeatAndJoinParsed = RepeatAndJoin(Any(*allowedChars)) { repeated: List<Any.AnyParsed> ->
            repeated.map { it.parsed as CharacterParsed }.map { it.char }.joinToString(separator = "")
        }.parse(parsed)

        return CustomWordParsed(parsed, repeatAndJoinParsed.joined.orElseGet { "" }, repeatAndJoinParsed.error)
    }
}