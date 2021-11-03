import java.util.*

class Beginning() : Parser {
    override fun getType() = "Beginning"

    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexStart
        return if (currentIndex.toInt() == -1) {
            context.copy(
                indexStart = -1,
                indexEnd = -1,
                context = hashMapOf(Pair("beginning", "reached")),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse character at index:[${currentIndex}], required:[beginning] but current index[$currentIndex]")
                ),
                type = "Beginning",
                context = emptyMap()
            )
        }
    }
}
