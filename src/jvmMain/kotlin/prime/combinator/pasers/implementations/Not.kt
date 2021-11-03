package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext
import prime.combinator.pasers.ParsingError
import java.util.*

class Not(
    private val parser: Parser
) : Parser {
    override fun getType() = "Not(" + parser.getType() + ")"

    override fun parse(context: ParsingContext): ParsingContext {
        val parsedContext = parser.parse(context)
        return if (parsedContext.success()) {
            parsedContext.copy(
                type = getType(),
                error = Optional.of(ParsingError("Expected not ${parser.getType()} but was it"))
            )
        } else {
            parsedContext.copy(
                type = getType(),
                error = Optional.empty()
            )
        }

    }
}