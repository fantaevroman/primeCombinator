package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext
import prime.combinator.pasers.ParsingError
import java.util.*

abstract class EndOfInputParser : Parser {
    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        return if (context.text.length - 1 < currentIndex) {
            context.copy(
                error = Optional.of(ParsingError("Can't parse at index:[${currentIndex}] end of text")),
                type = getType(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = emptyMap()
            )
        } else {
            parseNext(context)
        }
    }

    abstract fun parseNext(context: ParsingContext): ParsingContext
}