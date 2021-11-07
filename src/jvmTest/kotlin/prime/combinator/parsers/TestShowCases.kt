package prime.combinator.parsers

import org.junit.jupiter.api.Test
import prime.combinator.pasers.Parsed
import prime.combinator.pasers.implementations.*
import prime.combinator.pasers.implementations.Any
import prime.combinator.pasers.startParsing
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
            .parse(startParsing("http://combinator.primeframeworks.com"))
            .get()

        val protocol = (Any().fromSequence(parsedUrl.sequence, 0).anyOne as Str.StrParsed).str
        val domainName = CustomWord().fromSequence(parsedUrl.sequence, 2).customWord

        assertEquals("http", protocol)
        assertEquals("combinator.primeframeworks.com", domainName)
    }

    @Test
    fun testCustomParser() {
        class UrlParsed(val protocol: String, val domain: String, previous: Parsed, indexEnd: Long) :
            Parsed(previous, indexEnd)

        val parsedUrl = SequenceOf(
            Any(Str("http"), Str("https")),
            Str("://"),
            CustomWord(EnglishLetter().asChar(), Character('.'))
        ).map {
            val protocol = (Any().fromSequence(it.sequence, 0).anyOne as Str.StrParsed).str
            val domainName = CustomWord().fromSequence(it.sequence, 2).customWord

            UrlParsed(protocol, domainName, it, it.indexEnd)
        }.parse(startParsing("http://combinator.primeframeworks.com")).get()


        assertEquals("http", parsedUrl.protocol)
        assertEquals("combinator.primeframeworks.com", parsedUrl.domain)
    }

}