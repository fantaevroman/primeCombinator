class RepeatUntil(
    private val repeater: Parser,
    private val until: Parser
) : Parser {
    override fun getType() = "RepeatUntil"

    override fun parse(context: ParsingContext): ParsingContext {
        val repeaters = mutableListOf(repeater.parse(context))
        val untils = mutableListOf<ParsingContext>()

        while (true) {
            if (repeaters.last().fail()) {
                return repeaters.last().copy(
                    context = hashMapOf(Pair("repeaters", repeaters)),
                    type = "RepeatUntil"
                )
            }

            val currentUntil = until.parse(repeaters.last())
            if (currentUntil.success()) {
                untils.add(currentUntil)
                return repeaters.last().copy(
                    context = hashMapOf(
                        Pair("repeaters", repeaters),
                        Pair("untils", untils)
                    ),
                    type = "RepeatUntil"
                )
            }

            repeaters.add(repeater.parse(repeaters.last()))
        }
    }

    fun joinRepeaters(joinBetween: (contexts: List<ParsingContext>) -> ParsingContext): Parser {
        return this.map { original ->
            original.copy(
                context = hashMapOf(
                    Pair(
                        "repeater",
                        joinBetween(original.context["repeaters"] as List<ParsingContext>)
                    )
                )
            )
        }
    }
}
