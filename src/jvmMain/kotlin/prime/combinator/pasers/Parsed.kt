package prime.combinator.pasers
import java.util.*

open class Parsed(
    val text: String,
    val indexStart: Long,
    val indexEnd: Long,
    val context: Map<String, Any>,
    val type: String,
    val error: Optional<ParsingError>
) {
    fun fail() = error.isPresent
    fun success() = !error.isPresent
    override fun toString(): String {
        return "ParsingContext(type='$type', error=$error)"
    }

    fun currentIndex(): Long {
        return indexEnd + 1
    }

    fun textMaxIndex(): Int {
        return text.length - 1
    }

    companion object {
        fun asError(parsed: Parsed, message: String): Parsed {
            return Parsed(parsed.text, parsed.currentIndex(), parsed.currentIndex(), emptyMap(), parsed.type, Optional.of(ParsingError(message)))
        }
    }
}
