package prime.combinator.pasers.implementations
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Beginning.BeginningParsed
import java.util.*
import kotlin.Long

class Beginning : Parser<BeginningParsed> {
    inner class BeginningParsed(previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<BeginningParsed> {
        return if (previous.currentIndex() == -1L) {
            ParsedResult.asSuccess(BeginningParsed(previous, -1))
        } else {
            ParsedResult.asError("Not beginning of the text")
        }
    }
}
