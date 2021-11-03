package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext

class Word() : Parser {
    override fun getType() = "Word"

    override fun parse(context: ParsingContext): ParsingContext {
        return RepeatAndJoin(EnglishLetter()) { AnyCharacter.join(it, "", "letter") }
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