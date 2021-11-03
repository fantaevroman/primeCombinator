package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext
import prime.combinator.pasers.ParsingError
import java.util.*

class End() : Parser {
    override fun getType() = "End"

    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        return if (context.text.length - 1 < currentIndex) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("end", "reached")),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse character at index:[${currentIndex}], required:[end] but has more characters")
                ),
                type = "End",
                context = emptyMap()
            )
        }
    }
}
