package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.implementations.EnglishDigit.EnglishDigitParsed
import kotlin.Long

/**
 * EnglishDigit allows parsing only digits '1'..'9'
 * Example:
 *  aim: we want parse only digits
 *  how to reach:  EnglishDigit().parse("1").get()
 *  result: successfully parsed digit "1".
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class EnglishDigit : EndOfInputParser<EnglishDigitParsed>() {
    inner class EnglishDigitParsed(val digit: Short, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<EnglishDigitParsed> {
        val next = previous.text.toCharArray()[previous.currentIndex().toInt()]
        return if (((next in '1'..'9'))) {
            ParsedResult.asSuccess(EnglishDigitParsed(next.toString().toShort(), previous, previous.currentIndex()))
        } else {
            ParsedResult.asError("Can't parse English digit required:[1..9] but was:[$next]")
        }
    }
}
