package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Between.BetweenParsed
import java.util.*

class Between(
    private val left: Parser<out Parsed>,
    private val between: Parser<out Parsed>,
    private val right: Parser<out Parsed>,
) : Parser<BetweenParsed> {

    inner class BetweenParsed(
        val parsed: Parsed,
        val left: Parsed,
        val between: Parsed,
        val right: Parsed,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            left.indexStart,
            right.indexEnd,
            error.map { emptyMap<String, Parsed>() }.orElseGet {
                hashMapOf(
                    Pair("Left", left),
                    Pair("between", between),
                    Pair("right", right)
                )
            },
            getType(),
            error
        )

    override fun getType() = "Between"

    override fun parse(parsed: Parsed): BetweenParsed {
        val sequenceParsed = SequenceOf(left, between, right).parse(parsed)

        return if (sequenceParsed.success()) {
            BetweenParsed(
                parsed,
                sequenceParsed.sequence[0],
                sequenceParsed.sequence[1],
                sequenceParsed.sequence[2]
            )
        } else {
            BetweenParsed(parsed, parsed, parsed, parsed, sequenceParsed.error)
        }
    }
}