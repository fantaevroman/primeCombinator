package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.End.EndParsed
import kotlin.Long

class End() : Parser<EndParsed> {
    inner class EndParsed(previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<EndParsed> {
        return when {
            previous.indexEnd == -1L -> ParsedResult.asSuccess(
                EndParsed(
                    previous,
                    -1
                )
            )

            previous.textMaxIndex().toLong() == previous.indexEnd -> ParsedResult.asSuccess(
                EndParsed(
                    previous,
                    previous.currentIndex()
                )
            )
            previous.textMaxIndex().toLong() < previous.indexEnd -> ParsedResult.asError("Not end")
            else -> ParsedResult.asError("End index is shorter than current position")
        }
    }
}
