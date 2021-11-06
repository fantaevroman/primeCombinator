package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Not.NotParsed
import java.util.*
import kotlin.Long

class Not(
    private val parser: Parser<out Parsed>
) : Parser<NotParsed> {
    inner class NotParsed(previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<NotParsed> {
        val parsed = parser.parse(previous)
        return if (parsed.success()) {
            ParsedResult.asError("Expected failed on Not operation but was success")
        } else {
            ParsedResult.asSuccess(NotParsed(previous, previous.indexEnd))
        }
    }
}