package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext

open class RepeatAndJoin(
    val parser: Parser,
    val joinBetween: (contexts: List<ParsingContext>) -> ParsingContext
) : Parser {
    override fun getType() = "RepeatAndJoin"
    override fun parse(context: ParsingContext): ParsingContext =
        RepeatUntil(parser, Not(parser))
            .joinRepeaters {
                joinBetween(it)
            }
            .map {
                it.copy(
                    type = "RepeatAndJoin",
                    context = hashMapOf(
                        Pair(
                            "joint",
                            (it.context["repeater"] as ParsingContext).context["str"] as String
                        )
                    )
                )
            }.parse(context)
}