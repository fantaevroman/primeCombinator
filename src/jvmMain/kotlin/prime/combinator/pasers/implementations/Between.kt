package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import kotlin.Long

class Between<L : Parsed, M : Parsed, R : Parsed>(
    private val left: Parser<L>,
    private val between: Parser<M>,
    private val right: Parser<R>
) : Parser<Between<L, M, R>.BetweenParsed> {

    inner class BetweenParsed(
        val left: L,
        val between: M,
        val right: R,
        previous: Parsed,
        indexEnd: Long
    ) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<BetweenParsed> {
       return SequenceOf(left, between, right).map { sequenceParsed ->
           BetweenParsed(
               sequenceParsed.sequence[0] as L,
               sequenceParsed.sequence[1] as M,
               sequenceParsed.sequence[2] as R,
               previous,
               sequenceParsed.sequence[2].indexEnd
           )
       }.parse(previous)
    }
}