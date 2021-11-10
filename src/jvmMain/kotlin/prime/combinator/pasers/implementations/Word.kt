package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import kotlin.Long

/**
 * Word allows parsing any word
 * Example:
 *  aim: we want to parse first word in text "Name is ..."
 *  how to reach: Word().parse("Name is ...").get()
 *
 *  result: Word "Name" is parsed.
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
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

