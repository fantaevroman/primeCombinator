import java.util.*

class EnglishLetter : EndOfInputParser() {
    override fun getType() = "EnglishLetter"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        val next = context.text.toCharArray()[currentIndex.toInt()]
        return if (((next in 'a'..'z')) || ((next in 'A'..'Z'))) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("letter", next)),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse English letter at index:[${currentIndex}], required:[a..z, A..Z] but was:[$next]")
                ),
                type = getType(),
                context = emptyMap(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
            )
        }
    }

    fun asChar(): Parser {
        return this.map { it.copy(
            type = "Character",
            context = hashMapOf(Pair("character", it.context["letter"].toString()))) }
    }
}