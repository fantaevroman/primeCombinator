package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import kotlin.Long

/**
 * Spaces allows parsing several space at one time
 * Example:
 *  aim: we want to parse string spaces in the beginning of sting "   Name is ..."
 *  how to reach:  Spaces().parse("   Name is ...").get()
 *
 *  result: spaces presiding "Name" are parsed.
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class Spaces : EndOfInputParser<Spaces.SpacesParsed>() {
    inner class SpacesParsed(val spaces: String, mapFrom: Parsed) : Parsed(mapFrom)

    override fun parseNext(previous: Parsed): ParsedResult<SpacesParsed> {
        return Repeat(Character(' '))
            .joinRepeaters { it.map { it.char }.joinToString(separator = "") }
            .map {
                SpacesParsed(it.joined, it)
            }.parse(previous)
    }
}


