package prime.combinator.parsers

import org.junit.jupiter.api.Test
import prime.combinator.pasers.implementations.*
import prime.combinator.pasers.startParsing
import kotlin.test.assertEquals

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
            .joinRepeaters { it.map { it.letter }.joinToString (separator = "") }
            .parse(startParsing("[Na]")).get()
        assertEquals(0, repeatableBetweenParsed.indexStart)
        assertEquals(3, repeatableBetweenParsed.indexEnd)
        assertEquals("Na", repeatableBetweenParsed.between)
    }
}