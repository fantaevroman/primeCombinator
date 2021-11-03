package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext
import prime.combinator.pasers.ParsingError
import java.util.*

open class Str(val string: String) : Parser {
    override fun getType() = "Str"

    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        if (context.text.length - 1 < currentIndex - 1 + string.length) {
            return context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex + string.length - 1,
                error = Optional.of(ParsingError("Can't parse at index:[${currentIndex}] end of text")),
                type = "Str",
                context = emptyMap()
            )
        } else {
            val expectedIndex = currentIndex.toInt()
            val indexOf = context.text.indexOf(string, expectedIndex)
            return if (indexOf == expectedIndex) {
                context.copy(
                    indexStart = currentIndex,
                    indexEnd = currentIndex + string.length - 1,
                    context = hashMapOf(
                        Pair("str", string)
                    ),
                    type = "Str"
                )
            } else {
                context.copy(
                    indexStart = currentIndex,
                    indexEnd = currentIndex + string.length - 1,
                    error = Optional.of(ParsingError("Can't parse at index:[${currentIndex}] [$string] not found")),
                    type = "Str",
                    context = emptyMap()
                )
            }
        }
    }
}