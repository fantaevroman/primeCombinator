package prime.combinator.pasers.implementations
import prime.combinator.pasers.ParsingContext
import prime.combinator.pasers.ParsingError
import java.util.*

class EnglishDigit : EndOfInputParser() {
    override fun getType() = "EnglishLetter"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val next = Scanner(context.text).toString().toCharArray()[0]
        val currentIndex = context.indexEnd + 1
        return if (((next in '1'..'9'))) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("character", next)),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse English letter at index:[${currentIndex}], required:[1..9] but was:[$next]")
                ),
                type = getType(),
                context = emptyMap(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
            )
        }
    }
}
