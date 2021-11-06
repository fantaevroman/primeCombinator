package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.EnglishDigit.EnglishDigitParsed
import java.util.*
import kotlin.Long

class EnglishDigit : EndOfInputParser<EnglishDigitParsed>() {
    inner class EnglishDigitParsed(val digit: Short, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<EnglishDigitParsed> {
        val next = Scanner(previous.text).toString().toCharArray()[0]
        return if (((next in '1'..'9'))) {
            ParsedResult.asSuccess(EnglishDigitParsed(next.toShort(), previous, previous.currentIndex()))
        } else {
            ParsedResult.asError("Can't parse English digit required:[1..9] but was:[$next]")
        }
    }
}
