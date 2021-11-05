package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Any.AnyParsed
import java.util.*

class  Any(private vararg val parsers: Parser<*>) : EndOfInputParser<AnyParsed>() {
    inner class AnyParsed(
        val parsed: Parsed,
        val anyOne: Parsed,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            anyOne.indexStart,
            anyOne.indexEnd,
            error.map { emptyMap<String, Parsed>() }.orElseGet { hashMapOf(Pair(getType(), anyOne)) },
            getType(),
            error
        )

    override fun getType() = "Any"

    override fun parseNext(parsed: Parsed): AnyParsed {
        val iterator = parsers.iterator()
        while (iterator.hasNext()) {
            val parserResult = iterator.next().parse(parsed)
            if (parserResult.success()) {
                return AnyParsed(parsed, parserResult)
            }
        }

        return asError(
            parsed,
            "Non of supplied parsers matched:[${parsers.joinToString(separator = ",") { it.getType() }}]"
        )
    }

    override fun asError(parsed: Parsed, message: String): AnyParsed {
        return AnyParsed(parsed, Parsed.asError(parsed, message))
    }
}