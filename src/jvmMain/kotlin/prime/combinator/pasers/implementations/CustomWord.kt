package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Character.CharacterParsed
import prime.combinator.pasers.implementations.CustomWord.CustomWordParsed
import java.util.*
import kotlin.Long


/**
 * CustomWord allows specifying word which consists only from specified characters
 * Example:
 *  aim: we want parse word that has in it only these characters: "a", "b"
 *  how to reach:  CustomWord(Character('a'), Character('b')).parse("abc").get()
 *  result: successfully parsed word "ab" because text starts with "ab". "c" is not part of the word.
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class CustomWord(private vararg val allowedChars: Parser<CharacterParsed>) : Parser<CustomWordParsed> {
    inner class CustomWordParsed(val customWord: String, mapFrom: Parsed) : Parsed(mapFrom)

    override fun parse(previous: Parsed): ParsedResult<CustomWordParsed> {

        return Repeat(Any(*allowedChars)).joinRepeaters { repeated: List<Any.AnyParsed> ->
            repeated.map { it.anyOne as CharacterParsed }.map { it.char }.joinToString(separator = "")
        }.map {
            CustomWordParsed(it.joined, it)
        }.parse(previous)
    }
}