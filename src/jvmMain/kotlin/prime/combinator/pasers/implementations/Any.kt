package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext
import prime.combinator.pasers.ParsingError
import java.util.*

class Any(private vararg val parsers: Parser) : EndOfInputParser() {
    override fun getType() = "Any"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val iterator = parsers.iterator()
        while (iterator.hasNext()) {
            val parserResult = iterator.next().parse(context)
            if (parserResult.success()) {
                return parserResult
            }
        }

        return context.copy(
            error = Optional.of(
                ParsingError(
                    "Non of supplied parsers matched:[${parsers.joinToString(separator = ",") { it.getType() }}]"
                )
            )
        )
    }
}