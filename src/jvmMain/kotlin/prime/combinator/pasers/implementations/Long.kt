package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Long.LongParsed
import java.util.*
import kotlin.Long

class Long : EndOfInputParser<LongParsed>() {
    inner class LongParsed(
        val parsed: Parsed,
        val long: Long,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + 1,
            error.map { emptyMap<String, Long>() }.orElseGet { hashMapOf(Pair(getType(), long)) },
            getType(),
            error
        )

    override fun getType() = "Long"

    override fun parseNext(parsed: Parsed): LongParsed {
        val scanner = Scanner(parsed.text)

        return if (scanner.hasNextLong()) {
            LongParsed(parsed, scanner.nextLong())
        } else {
            asError(parsed, "Can't parse Long at index:[${parsed.currentIndex()}]")
        }
    }

    override fun asError(parsed: Parsed, message: String): LongParsed {
        return LongParsed(parsed, 0, Optional.of(ParsingError(message)))
    }
}