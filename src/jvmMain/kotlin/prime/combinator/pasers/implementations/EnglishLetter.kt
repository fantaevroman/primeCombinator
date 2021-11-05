package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.EnglishLetter.EnglishLetterParsed
import java.util.*

class EnglishLetter : EndOfInputParser<EnglishLetterParsed>() {

    inner class EnglishLetterParsed(
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

    override fun getType() = "EnglishLetter"

    override fun parseNext(parsed: Parsed): EnglishLetterParsed {
        val next = parsed.text.toCharArray()[parsed.currentIndex().toInt()]
        return if (((next in 'a'..'z')) || ((next in 'A'..'Z'))) {
            EnglishLetterParsed(parsed, next)
        } else {
            asError(parsed, "[$next] not an english letter")
        }
    }

    override fun asError(parsed: Parsed, message: String): EnglishLetterParsed {
        return EnglishLetterParsed(parsed, 'e', Optional.of(ParsingError(message)))
    }
}