import java.util.*

class Long : EndOfInputParser() {
    override fun getType() = "Long"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val scanner = Scanner(context.text)
        val currentIndex = context.indexEnd + 1

        return if (scanner.hasNextLong()) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(
                    Pair("longValue", scanner.nextInt())
                )
            )
        } else {
            context.copy(
                error = Optional.of(ParsingError("Can't parse Long at index:[${currentIndex}]")),
                type = "Long",
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = emptyMap()
            )
        }
    }
}