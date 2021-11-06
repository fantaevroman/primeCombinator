package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.Any.AnyParsed
import kotlin.Long

class Any(private vararg val parsers: Parser<*>) : EndOfInputParser<AnyParsed>() {
    inner class AnyParsed(previous: Parsed, val anyOne: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<AnyParsed> {
        val iterator = parsers.iterator()
        while (iterator.hasNext()) {
            val parserResult = iterator.next().parse(previous)
            if (parserResult.success()) {
                parserResult.map {
                    AnyParsed(previous, it, it.indexEnd)
                }.get()
            }
        }

        return ParsedResult.asError("No of supplied parsed matched at Any operation")
    }
}