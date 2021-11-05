package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import java.util.*

class End() : Parser<Parsed> {

    inner class EndParsed(
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

    override fun getType() = "End"

    override fun parse(parsed: Parsed): Parsed {
        return when{
            parsed.textMaxIndex().toLong() == parsed.currentIndex() -> EndParsed(parsed)
            parsed.textMaxIndex().toLong() < parsed.currentIndex() -> EndParsed(parsed, Optional.of(ParsingError("Not end")))
            else -> EndParsed(parsed, Optional.of(ParsingError("End index is shorter than current position")))
        }
    }
}
