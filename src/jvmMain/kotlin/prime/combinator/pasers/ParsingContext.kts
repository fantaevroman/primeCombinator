import java.util.*

data class ParsingContext(
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
}
