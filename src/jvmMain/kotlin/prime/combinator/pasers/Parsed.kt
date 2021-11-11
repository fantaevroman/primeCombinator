package prime.combinator.pasers

import java.util.*

/**
 * Parsed represent a result of successfully worked parser.
 * text - originial text parser worked on (whole text)
 * indexStart - from which point parser started working
 * indexEnd - at which point parser stopped working

 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
open class Parsed(
    val text: String,
    val indexStart: Long,
    val indexEnd: Long
) {
    constructor(mappedFrom: Parsed) : this(mappedFrom.text, mappedFrom.indexStart, mappedFrom.indexEnd)
    constructor(previous: Parsed, indexEnd: Long) : this(previous.text, previous.currentIndex(), indexEnd)

    fun currentIndex(): Long {
        return indexEnd + 1
    }

    fun textMaxIndex(): Int {
        return text.length - 1
    }
}

/**
 * Parsed represent a result of worked parser.
 * parsed - in case of successful parsing, contains indexes of parsed part of the text
 * error - in case error happened during parsing, contains error message
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class ParsedResult<T : Parsed>(val parsed: Optional<T>, val error: Optional<String>) {

    fun success(): Boolean {
        return !error.isPresent
    }

    fun <A : Parsed> map(mapper: (T: Parsed) -> A): ParsedResult<A> {
        return if (success()) {
            asSuccess(mapper(this.parsed.get()))
        } else {
            asError(error.get())
        }
    }

    fun get(): T {
        if (success()) {
            return parsed.get()
        } else {
            throw RuntimeException("Can't perform get on failed ParsedResult, error=[${error.get()}]")
        }
    }

    companion object {
        fun <T : Parsed> asSuccess(parsed: T): ParsedResult<T> {
            return ParsedResult(Optional.of(parsed), Optional.empty())
        }

        fun <T : Parsed> asError(message: String): ParsedResult<T> {
            return ParsedResult(Optional.empty(), Optional.of(message))
        }
    }
}