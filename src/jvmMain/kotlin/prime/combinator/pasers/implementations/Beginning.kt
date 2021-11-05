package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Beginning.BeginningParsed
import java.util.*

class Beginning() : Parser<BeginningParsed> {
    inner class BeginningParsed(
        val parsed: Parsed,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + 1,
            error.map { emptyMap<String, Boolean>() }.orElseGet { hashMapOf(Pair(getType(), true)) },
            getType(),
            error
        )

    override fun getType() = "Beginning"

    override fun parse(parsed: Parsed): BeginningParsed {
        return if (parsed.currentIndex() == -1L) {
            BeginningParsed(parsed)
        } else {
            BeginningParsed(parsed, Optional.of(ParsingError("Not beginning of the text")))
        }
    }
}
