class RepeatableBetween(
    private val left: Parser,
    private val between: Parser,
    private val right: Parser,
    private val sequenceOf: Parser = SequenceOf(left, RepeatUntil(between, right), right)
) : Parser {
    override fun getType() = "RepeatableBetween"

    override fun parse(context: ParsingContext): ParsingContext {
        return sequenceOf.map {
            val between = (it.context["sequence"] as List<ParsingContext>)[1]
            val repeaters = (between.context["repeaters"] as List<ParsingContext>)
            it.copy(
                context = hashMapOf(
                    Pair("between", repeaters),
                    Pair("left", (it.context["sequence"] as List<ParsingContext>)[0]),
                    Pair("right", (it.context["sequence"] as List<ParsingContext>)[2])
                )
            )
        }.parse(context).copy(
            type = getType(),
            indexStart = context.indexStart + 1
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
        return this.map { original ->
            original.copy(
                context =
                hashMapOf(
                    Pair("left", original.context["left"]!!),
                    Pair("right", original.context["right"]!!),
                    Pair(
                        "between",
                        joinBetween(original.context["between"] as List<ParsingContext>)
                    )
                )
            )
        }
    }

    fun joinBetweenCharsToStrings() = this.map { repeatableContext ->
        val between = repeatableContext.context["between"] as List<ParsingContext>
        fun isString(c: ParsingContext) = c.type == "AnyCharacter" || c.type == "Str"
        val joined = mutableListOf<ParsingContext>()

        for (r in between) {
            if (joined.isEmpty()) {
                joined.add(r)
                continue
            } else {
                val l = joined.last()
                if (isString(l) && isString(r)) {
                    joined.removeLast()
                    joined.add(
                        l.copy(
                            type = "Str",
                            context = hashMapOf(
                                Pair(
                                    "str",
                                    l.context[if (l.type == "AnyCharacter") "anyCharacter" else "str"].toString() + r.context["anyCharacter"]
                                )
                            )
                        )
                    )
                } else {
                    joined.add(r)
                }
            }
        }
        repeatableContext.copy(context = mapOf(Pair("between", joined)))
    }
}
