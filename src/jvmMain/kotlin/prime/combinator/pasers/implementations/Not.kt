package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Not.NotParsed
import java.util.*

class Not(
    private val parser: Parser<out Parsed>
) : Parser<NotParsed> {

    inner class NotParsed(
        val parsed: Parsed,
        val not: Parsed,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + 1,
            error.map { emptyMap<String, Parsed>() }.orElseGet { hashMapOf(Pair("not", not)) },
            getType(),
            error
        )

    override fun getType() = "Not(" + parser.getType() + ")"

    override fun parse(parsed: Parsed): NotParsed {
        val parsedContext = parser.parse(parsed)
        return if (parsedContext.success()) {
            NotParsed(parsed, parsedContext, Optional.of(ParsingError("Expected not ${parser.getType()} but was it")))
        } else {
            NotParsed(parsed, parsedContext)
        }
    }
}