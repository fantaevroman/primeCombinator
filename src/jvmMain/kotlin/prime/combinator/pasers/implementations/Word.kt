package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.EnglishLetter.EnglishLetterParsed
import java.util.*
import kotlin.Long

class Word() : Parser<Word.WordParsed> {
    inner class WordParsed(val word: String, previous: Parsed, indexEnd: Long) : Parsed(previous, indexEnd)

    override fun parse(previous: Parsed): ParsedResult<WordParsed> {
        return RepeatAndJoin(EnglishLetter()) { repeated: List<EnglishLetterParsed> ->
            repeated.map { it.letter }.joinToString(separator = "")
        }.map {
            val word = it.joined.get()
            WordParsed(word, previous, previous.indexEnd + word.length)
        }.parse(previous)
    }
}

