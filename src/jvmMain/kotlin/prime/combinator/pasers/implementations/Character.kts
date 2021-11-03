import java.util.*

class Character(private val char: Char) : EndOfInputParser() {
    override fun getType() = "Character"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        val next = context.text.toCharArray()[currentIndex.toInt()]
        return if (next == char) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("character", next)),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse character at index:[${currentIndex}], required:[$char] but was:[$next]")
                ),
                type = getType(),
                context = emptyMap(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
            )
        }
    }
}
