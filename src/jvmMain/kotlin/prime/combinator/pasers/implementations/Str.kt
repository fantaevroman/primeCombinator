package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Str.StrParsed
import java.util.*
import kotlin.Long

open class Str(val string: String) : Parser<StrParsed> {
    inner class StrParsed(
        val parsed: Parsed,
        val str: String,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + string.length - 1,
            error.map { emptyMap<String, String>() }.orElseGet { hashMapOf(Pair(getType(), str)) },
            getType(),
            error
        )

    override fun getType() = "Str"

    override fun parse(parsed: Parsed): StrParsed {
        return if (parsed.textMaxIndex() < parsed.currentIndex() + string.length) {
            asError(parsed, "Cant parse string, end of text")
        } else {
            val expectedIndex = parsed.currentIndex().toInt()
            val indexOf = parsed.text.indexOf(string, expectedIndex)
            return if (indexOf == expectedIndex) {
                StrParsed(parsed, string)
            } else {
                asError(parsed, "Can't parse, [$string] not found")
            }
        }
    }

    fun asError(parsed: Parsed, message: String): StrParsed {
        return StrParsed(parsed, "error", Optional.of(ParsingError(message)))
    }
}