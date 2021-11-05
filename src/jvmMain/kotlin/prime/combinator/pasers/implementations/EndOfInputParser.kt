package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser

abstract class EndOfInputParser<T : Parsed> : Parser<T> {
    override fun parse(parsed: Parsed): T {
        return if (parsed.currentIndex() > parsed.textMaxIndex()) {
            asError(parsed, "Can't parse: end of text")
        } else {
            parseNext(parsed)
        }
    }

    abstract fun parseNext(parsed: Parsed): T
    abstract fun asError(parsed: Parsed, message: String): T
}