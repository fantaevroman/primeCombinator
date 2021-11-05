package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.EnglishDigit.EnglishDigitParsed
import java.util.*

class EnglishDigit : EndOfInputParser<EnglishDigitParsed>() {

    inner class EnglishDigitParsed(
        val parsed: Parsed,
        val short: Short,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + 1,
            error.map { emptyMap<String, Short>() }.orElseGet { hashMapOf(Pair(getType(), short)) },
            getType(),
            error
        )

    override fun getType() = "EnglishLetter"

    override fun parseNext(parsed: Parsed): EnglishDigitParsed {
        val next = Scanner(parsed.text).toString().toCharArray()[0]
        return if (((next in '1'..'9'))) {
            EnglishDigitParsed(parsed, next.toShort())
        } else {
            asError(parsed, "Can't parse English digit required:[1..9] but was:[$next]")
        }
    }

    override fun asError(parsed: Parsed, message: String): EnglishDigitParsed {
        return EnglishDigitParsed(parsed, 0, Optional.of(ParsingError(message)))
    }
}
