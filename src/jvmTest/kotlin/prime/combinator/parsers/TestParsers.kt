package prime.combinator.parsers

import org.junit.jupiter.api.Test
import prime.combinator.pasers.implementations.*
import prime.combinator.pasers.startParsing
import kotlin.test.assertEquals

/**
 * Common tests for all parsers in lib
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
class TestParsers {

    @Test
    fun testStr() {
        val parsedName = Str("Name").parse(startParsing("Name is ...")).get()
        assertEquals(parsedName.str, "Name")
        assertEquals(parsedName.indexStart, 0)
        assertEquals(parsedName.indexEnd, 3)
    }

    @Test
    fun testWord() {
        val parsedWord = Word().parse(startParsing("Name is ...")).get()
        assertEquals(parsedWord.word, "Name")
        assertEquals(parsedWord.indexStart, 0)
        assertEquals(parsedWord.indexEnd, 3)
    }

    @Test
    fun testSpaces() {
        val parsedSpaces = Spaces().parse(startParsing("   Name is ...")).get()
        assertEquals(parsedSpaces.spaces, "   ")
        assertEquals(parsedSpaces.indexStart, 0)
        assertEquals(parsedSpaces.indexEnd, 2)
    }

    @Test
    fun testSequenceOf() {
        val parsedSequenceOf = SequenceOf(Beginning(), Spaces(), Word(), Spaces(), Word(), End())
            .parse(startParsing("   Name is")).get()

        val beginning = Beginning().fromSequence(parsedSequenceOf.sequence, 0)
        val spaces = Spaces().fromSequence(parsedSequenceOf.sequence, 1)
        val name = Word().fromSequence(parsedSequenceOf.sequence, 2)
        val spaces2 = Spaces().fromSequence(parsedSequenceOf.sequence, 3)
        val wordIs = Word().fromSequence(parsedSequenceOf.sequence, 4)
        val end = End().fromSequence(parsedSequenceOf.sequence, 5)

        assertEquals(-1, beginning.indexEnd)
        assertEquals(2, spaces.indexEnd)
        assertEquals("   ", spaces.spaces)
        assertEquals(3, name.indexStart)
        assertEquals(6, name.indexEnd)
        assertEquals("Name", name.word)
        assertEquals(" ", spaces2.spaces)
        assertEquals("is", wordIs.word)
        assertEquals(10, end.indexEnd)
    }

    @Test
    fun testRepeatUntil() {
        val repeatUntilParsed = RepeatUntil(Character('a'), Character('b'))
            .parse(startParsing("aaab")).get()
        assertEquals(0, repeatUntilParsed.indexStart)
        assertEquals(3, repeatUntilParsed.indexEnd)
        assertEquals(3, repeatUntilParsed.untilParsed.indexStart)
        assertEquals(3, repeatUntilParsed.untilParsed.indexEnd)
        assertEquals('b', repeatUntilParsed.untilParsed.char)
        assertEquals(3, repeatUntilParsed.repeatersParsed.size)
        assertEquals('a', repeatUntilParsed.repeatersParsed[0].char)
        assertEquals('a', repeatUntilParsed.repeatersParsed[1].char)
        assertEquals('a', repeatUntilParsed.repeatersParsed[2].char)
    }


    @Test
    fun testRepeat() {
        val repeatableBetweenParsed = Repeat(EnglishLetter())
            .parse(startParsing("Name1")).get()
        assertEquals(0, repeatableBetweenParsed.indexStart)
        assertEquals(3, repeatableBetweenParsed.indexEnd)
        assertEquals(4, repeatableBetweenParsed.repeatersParsed.size)
    }

    @Test
    fun testRepeatJoined() {
        val repeatableBetweenParsed = Repeat(EnglishLetter())
            .joinRepeaters { it.map { it.letter }.joinToString(separator = "") }
            .parse(startParsing("Name1")).get()
        assertEquals(0, repeatableBetweenParsed.indexStart)
        assertEquals(3, repeatableBetweenParsed.indexEnd)
        assertEquals("Name", repeatableBetweenParsed.joined)
    }


    @Test
    fun testRepeatableBetween() {
        val repeatableBetweenParsed = RepeatableBetween(Str("["), EnglishLetter(), Str("]"))
            .parse(startParsing("[Na]")).get()
        assertEquals(0, repeatableBetweenParsed.indexStart)
        assertEquals(3, repeatableBetweenParsed.indexEnd)
        assertEquals("[", repeatableBetweenParsed.left.str)
        assertEquals("]", repeatableBetweenParsed.right.str)
        assertEquals(2, repeatableBetweenParsed.between.size)
        assertEquals('N', repeatableBetweenParsed.between[0].letter)
        assertEquals('a', repeatableBetweenParsed.between[1].letter)
    }

    @Test
    fun testRepeatableBetweenJoined() {
        val repeatableBetweenParsed = RepeatableBetween(Str("["), EnglishLetter(), Str("]"))
            .joinRepeaters { it.map { it.letter }.joinToString(separator = "") }
            .parse(startParsing("[Na]")).get()
        assertEquals(0, repeatableBetweenParsed.indexStart)
        assertEquals(3, repeatableBetweenParsed.indexEnd)
        assertEquals("Na", repeatableBetweenParsed.between)
    }

    @Test
    fun testNot() {
        val notA = Not(Str("a")).parse(startParsing("b")).get()
        assertEquals(0, notA.indexStart)
    }

    @Test
    fun testLong() {
        val longParsed = Long().parse(startParsing("1")).get()
        assertEquals(0, longParsed.indexStart)
        assertEquals(0, longParsed.indexEnd)
        assertEquals(1L, longParsed.long)
    }

    @Test
    fun testEnglishLetter() {
        val englishLetterParsed = EnglishLetter().parse(startParsing("a")).get()
        assertEquals(0, englishLetterParsed.indexStart)
        assertEquals(0, englishLetterParsed.indexEnd)
        assertEquals('a', englishLetterParsed.letter)
    }

    @Test
    fun testEnglishDigit() {
        val englishDigitParsed = EnglishDigit().parse(startParsing("1")).get()
        assertEquals(0, englishDigitParsed.indexStart)
        assertEquals(0, englishDigitParsed.indexEnd)
        assertEquals(1, englishDigitParsed.digit)
    }

    @Test
    fun testEnd() {
        val endParsed = End().parse(startParsing("")).get()
        assertEquals(0, endParsed.indexStart)
        assertEquals(0, endParsed.indexEnd)
    }

    @Test
    fun testDoubleQuote() {
        val doubleQuoteParsed = DoubleQuote().parse(startParsing(""""""")).get()
        assertEquals(0, doubleQuoteParsed.indexStart)
        assertEquals(0, doubleQuoteParsed.indexEnd)
        assertEquals(""""""", doubleQuoteParsed.str)
    }

    @Test
    fun testCustomWord() {
        val customWordParsed = CustomWord(Character('a'), Character('b'))
            .parse(startParsing("abc")).get()
        assertEquals(0, customWordParsed.indexStart)
        assertEquals(1, customWordParsed.indexEnd)
        assertEquals("ab", customWordParsed.customWord)
    }

    @Test
    fun testBetween() {
        val betweenParsed = Between(Character('['), Character('b'), Character(']'))
            .parse(startParsing("[b]")).get()
        assertEquals(0, betweenParsed.indexStart)
        assertEquals(2, betweenParsed.indexEnd)
        assertEquals('b', betweenParsed.between.char)
        assertEquals('[', betweenParsed.left.char)
        assertEquals(']', betweenParsed.right.char)
    }

    @Test
    fun testBeginning() {
        val beginningParsing = Beginning()
            .parse(startParsing("")).get()
        assertEquals(0, beginningParsing.indexStart)
        assertEquals(-1, beginningParsing.indexEnd)
    }

    @Test
    fun testAnyCharacter() {
        val anyCharacterParsed = AnyCharacter()
            .parse(startParsing("a")).get()
        assertEquals(0, anyCharacterParsed.indexStart)
        assertEquals(0, anyCharacterParsed.indexEnd)
        assertEquals('a', anyCharacterParsed.char)
    }

    @Test
    fun testCharacter() {
        val characterParsed = Character('a')
            .parse(startParsing("a")).get()
        assertEquals(0, characterParsed.indexStart)
        assertEquals(0, characterParsed.indexEnd)
        assertEquals('a', characterParsed.char)
    }

    @Test
    fun testAny() {
        val anyParsed = Any(Character('a'), Character('b'))
            .parse(startParsing("b")).get()
        assertEquals(0, anyParsed.indexStart)
        assertEquals(0, anyParsed.indexEnd)
        assertEquals('b', (anyParsed.anyOne as Character.CharacterParsed).char)
    }

}