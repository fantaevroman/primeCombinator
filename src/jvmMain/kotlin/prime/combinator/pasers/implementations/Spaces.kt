package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext

class Spaces() : Parser {
    override fun getType() = "Spaces"
    override fun parse(context: ParsingContext): ParsingContext =
        RepeatUntil(Character(' '), Not(Character(' ')))
            .joinRepeaters {
                AnyCharacter.join(it, "", "space")
            }
            .map {
                it.copy(
                    type = "Spaces",
                    context = hashMapOf(
                        Pair(
                            "spaces",
                            (it.context["repeater"] as ParsingContext).context["str"] as String
                        )
                    )
                )
            }.parse(context)
}
