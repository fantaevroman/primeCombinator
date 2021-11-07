package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Long.LongParsed
import java.util.*
import kotlin.Long

class Long : EndOfInputParser<LongParsed>() {
    inner class LongParsed(val long: Long, previous: Parsed) : Parsed(previous, previous.currentIndex())

    override fun parseNext(previous: Parsed): ParsedResult<LongParsed> {
        val scanner = Scanner(previous.text)

        return if (scanner.hasNextLong()) {
            ParsedResult.asSuccess(LongParsed(scanner.nextLong(), previous))
        } else {
            ParsedResult.asError("Can't parse Long")
        }
    }
}