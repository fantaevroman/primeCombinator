package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.RepeatableBetween.RepeatableBetweenParsed
import java.util.*

class RepeatableBetween<L : Parsed, M : Parsed, R : Parsed>(
    private val left: Parser<L>,
    private val between: Parser<M>,
    private val right: Parser<R>
) : Parser<RepeatableBetween<L, M, R>.RepeatableBetweenParsed> {

    inner class RepeatableBetweenParsed(
        val parsed: Parsed,
        val left: L,
        val between: List<M>,
        val right: R,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            left.indexStart,
            right.indexEnd,
            emptyMap(),
            getType(),
            error
        )

    override fun getType() = "RepeatableBetween"

    override fun parse(parsed: Parsed): RepeatableBetweenParsed {
          Between(left, RepeatUntil(between, right), right).map(
              {success ->
                  RepeatableBetweenParsed(parsed, success.left as L ,
                      success.between as List<M>,
                      success.right as R)
              },
              {fail ->  }
          )


    }

    fun mapEach(
        transformer: (currentContext: ParsingContext, currentParser: Parser, previous: List<ParsingContext>, currentIndex: Int) -> Parser
    ) = RepeatableBetween(left, between, right,
        SequenceOf(left, RepeatUntil(between, right), right)
            .mapEach { currentContext, currentParser, previous, currentIndex ->
                transformer(currentContext, currentParser, previous, currentIndex)
            }
    )


    fun joinBetween(joinBetween: (contexts: List<ParsingContext>) -> ParsingContext): Parser {
    }

    fun joinBetweenCharsToStrings() = this.map { repeatableContext ->
    }
}
