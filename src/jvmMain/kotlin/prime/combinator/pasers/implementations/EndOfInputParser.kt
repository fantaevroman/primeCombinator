package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser

abstract class EndOfInputParser<T : Parsed> : Parser<T> {
    override fun parse(previous: Parsed): ParsedResult<T> {
        return if (previous.currentIndex() > previous.textMaxIndex()) {
            ParsedResult.asError("Unexpected end of input")
        } else {
            parseNext(previous)
        }
    }

    abstract fun parseNext(previous: Parsed): ParsedResult<T>
}