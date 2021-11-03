import java.util.*

class AnyCharacter : EndOfInputParser() {
    override fun getType() = "AnyCharacter"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        return context.copy(
            indexStart = currentIndex,
            indexEnd = currentIndex,
            context = hashMapOf(Pair("anyCharacter", context.text[currentIndex.toInt()])),
            type = getType()
        )
    }

    companion object {
        fun join(
            list: List<ParsingContext>,
            separator: String = "",
            contextBodyName: String = "anyCharacter"
        ): ParsingContext {
            return if (list.isEmpty()) {
                return ParsingContext("empty join Str", 0, 0, emptyMap(), "emptyJoin", Optional.empty())
            } else {
                list.last().copy(
                    type = "Str",
                    context = hashMapOf(
                        Pair(
                            "str",
                            list.map { anyCharContext -> anyCharContext.context[contextBodyName] }
                                .joinToString(separator = separator)
                        )
                    )
                )
            }
        }
    }
}