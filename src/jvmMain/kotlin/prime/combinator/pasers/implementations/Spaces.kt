package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import kotlin.Long


class Spaces : EndOfInputParser<Spaces.SpacesParsed>() {
    inner class SpacesParsed(val spaces: String, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parseNext(previous: Parsed): ParsedResult<SpacesParsed> {
        return Repeat(Character(' '))
            .joinRepeaters { it.map { it.char }.joinToString(separator = "") }
            .map {
                SpacesParsed(it.joined, previous, it.indexEnd)
            }.parse(previous)
    }
}


