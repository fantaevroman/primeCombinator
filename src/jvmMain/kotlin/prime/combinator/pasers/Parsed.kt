package prime.combinator.pasers

import java.util.*

open class Parsed(
    val text: String,
    val indexStart: Long,
    val indexEnd: Long
) {
    constructor(previous: Parsed, indexEnd: Long) : this(previous.text, previous.currentIndex(), indexEnd)

    fun currentIndex(): Long {
        return indexEnd + 1
    }

    fun textMaxIndex(): Int {
        return text.length - 1
    }
}


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

    fun get(): T{
        if(success()){
            return parsed.get()
        }else{
            throw RuntimeException("Can't perform get on failed ParsedResult")
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