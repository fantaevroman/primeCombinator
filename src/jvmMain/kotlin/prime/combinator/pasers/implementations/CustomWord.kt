package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext

class CustomWord(
    vararg val parsers: Parser,
    val joinBetween: (contexts: List<ParsingContext>) -> ParsingContext = { AnyCharacter.join(it, "", "character") }
) : Parser {
    override fun getType() = "CustomWord"

    override fun parse(context: ParsingContext): ParsingContext {
        return RepeatAndJoin(Any(*parsers)) { joinBetween(it) }
            .map {
                it.copy(
                    type = getType(),
                    context = hashMapOf(
                        Pair(
                            "word",
                            it.context["joint"]!!
                        )
                    )
                )
            }.parse(context)
    }
}