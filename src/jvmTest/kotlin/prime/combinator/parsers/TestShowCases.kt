package prime.combinator.parsers

import org.junit.jupiter.api.Test
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.implementations.*
import prime.combinator.pasers.implementations.Any
import kotlin.test.assertEquals

/**
 * Advanced showcases for primeCombinator
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class TestShowCases {

    @Test
    fun testParseName() {
        val parsedUrl = SequenceOf(
            Any(Str("http"), Str("https")),
            Str("://"),
            CustomWord(EnglishLetter().asChar(), Character('.'))
        )
            .parse("http://combinator.primeframeworks.com")
            .get()

        val protocol = (Any().fromSequence(parsedUrl.sequence, 0).anyOne as Str.StrParsed).str
        val domainName = CustomWord().fromSequence(parsedUrl.sequence, 2).customWord

        assertEquals("http", protocol)
        assertEquals("combinator.primeframeworks.com", domainName)
    }

    @Test
    fun testCustomParser() {
        class UrlParsed(val protocol: String, val domain: String, mappedFrom: Parsed) : Parsed(mappedFrom, mappedFrom.indexEnd)

        val urlParser = SequenceOf(
            Any(Str("http"), Str("https")),
            Str("://"),
            CustomWord(EnglishLetter().asChar(), Character('.'))
        ).map { sequenceParserOutput->
            val protocol = (Any().fromSequence(sequenceParserOutput.sequence, 0).anyOne as Str.StrParsed).str
            val domainName = CustomWord().fromSequence(sequenceParserOutput.sequence, 2).customWord

            UrlParsed(protocol, domainName, sequenceParserOutput)
        }

        val urlParsed = urlParser.parse("http://combinator.primeframeworks.com").get()

        assertEquals("http", urlParsed.protocol)
        assertEquals("combinator.primeframeworks.com", urlParsed.domain)

        SequenceOf(Str("url: "), urlParser).parse("url: http://combinator.primeframeworks.com")
    }

    @Test
    fun testAny() {
        val anyParsed = Any(Word(), EnglishDigit()).parse("1 is not a name").get()
        assertEquals(0, anyParsed.indexStart)
        assertEquals(0, anyParsed.indexEnd)
        assertEquals(1, (anyParsed.anyOne as EnglishDigit.EnglishDigitParsed).digit)
    }


    @Test
    fun testParseDocument() {
        val document =  SequenceOf(Beginning(), Repeat(Any(Word(), Spaces(), EnglishDigit())), End()).parse("1 is not a name").get()
        val repeatParserOutput = document.sequence[1] as Repeat<Any.AnyParsed>.RepeatParsed
        val eightParserResultInsideRepeat = repeatParserOutput.repeatersParsed[8].anyOne as Word.WordParsed
        assertEquals("name", eightParserResultInsideRepeat.word)
    }

    @Test
    fun testCustomWord() {
        val document =  CustomWord(EnglishLetter().asChar(), Character('.')).parse("my.domain.com").get()
        assertEquals("my.domain.com", document.customWord)
    }
}