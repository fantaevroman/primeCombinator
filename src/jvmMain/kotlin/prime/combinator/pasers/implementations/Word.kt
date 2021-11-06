package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import kotlin.Long

class Word() : Parser<Word.WordParsed> {
    inner class WordParsed(val word: String, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<WordParsed> {
        return Repeat(EnglishLetter()).joinRepeaters {
            it.map { it.letter }.joinToString(separator = "")
        }.map {
            WordParsed(it.joined, previous, it.indexEnd)
        }.parse(previous)
    }
}

