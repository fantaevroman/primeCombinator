package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.Character.CharacterParsed
import prime.combinator.pasers.implementations.CustomWord.CustomWordParsed
import java.util.*
import kotlin.Long

class CustomWord(private vararg val allowedChars: Parser<CharacterParsed>) : Parser<CustomWordParsed> {
    inner class CustomWordParsed(val customWord: String, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<CustomWordParsed> {

        return Repeat(Any(*allowedChars)).joinRepeaters { repeated: List<Any.AnyParsed> ->
            repeated.map { it.anyOne as CharacterParsed }.map { it.char }.joinToString(separator = "")
        }.map {
            CustomWordParsed(
                it.joined,
                previous,
                previous.indexEnd + it.joined.length
            )
        }.parse(previous)
    }
}