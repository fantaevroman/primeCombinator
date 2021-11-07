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
    fun testSequanceOf() {
        val parsedSequenceOf =
            SequenceOf(Beginning(), Spaces(), Word(), Spaces(), Word(), End())
                .parse(startParsing("   Name is")).get()


        val parsedBeginning = parsedSequenceOf.sequence[0]
        val parsedSpaces = parsedSequenceOf.sequence[0]
        val parsedWord = parsedSequenceOf.sequence[0]

        assertEquals(parsedSequenceOf.sequence[0]., "   ")
        assertEquals(parsedSpaces.indexStart, 0)
        assertEquals(parsedSpaces.indexEnd, 2)
    }


}