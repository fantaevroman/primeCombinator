package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.ParsedResult
import prime.combinator.pasers.Parser
import prime.combinator.pasers.implementations.SequenceOf.SequenceOfParsed
import kotlin.Long

/**
 * SequenceOf allows parsing several parsers as a sequence
 * Example:
 *  aim: we want to parse string "   Name is" which consists of spaces and words
 *  how to reach:  SequenceOf(Beginning(), Spaces(), Word(), Spaces(), Word(), End())
 *  .parse(startParsing("   Name is")).get()
 *  result: successfully string.
 *
 *  We can reach each parser result as parsedSequenceOf.sequence.
 *  Each Parser has convinient method to convert type of result "fromSequence".
 *  See example here: TestParsers#testSequenceOf.
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
open class SequenceOf(vararg val parsers: Parser<out Parsed>) : Parser<SequenceOfParsed> {
    inner class SequenceOfParsed(
        val sequence: List<Parsed>,
        previous: Parsed,
        indexEnd: Long
    ) : Parsed(previous, indexEnd)


    override fun parse(previous: Parsed): ParsedResult<SequenceOfParsed> {
        val successSequence = mutableListOf<Parsed>()
        val parsersIterator = parsers.iterator().withIndex()
        var currentContext = previous

        while (parsersIterator.hasNext()) {
            val nextParser = parsersIterator.next()
            val nextTransformedParser =
                oneEachTransform(nextParser.index, currentContext, nextParser.value, successSequence)

            val currentResult = nextTransformedParser.parse(currentContext)
            if (currentResult.success()) {
                currentContext = currentResult.get()
                successSequence.add(currentContext)
            } else {
                return ParsedResult.asError(currentResult.error.get())
            }
        }

        return ParsedResult.asSuccess(
            SequenceOfParsed(
                successSequence,
                successSequence.last(),
                successSequence.last().indexEnd
            )
        )
    }

    open fun oneEachTransform(
        index: Int,
        currentParsed: Parsed,
        currentParser: Parser<out Parsed>,
        previous: List<out Parsed>,
    ): Parser<out Parsed> {
        return currentParser
    }

    fun mapEach(
        transformer: (
            currentParsed: Parsed,
            currentParser: Parser<out Parsed>,
            previousParsed: List<Parsed>,
            currentIndex: Int
        ) -> Parser<out Parsed>
    ) = object : SequenceOf(*this.parsers) {

        override fun parse(previous: Parsed): ParsedResult<SequenceOfParsed> {
            return super.parse(previous)
        }

        override fun oneEachTransform(
            index: Int,
            currentParsed: Parsed,
            currentParser: Parser<out Parsed>,
            previous: List<Parsed>,
        ): Parser<out Parsed> {
            return transformer(currentParsed, currentParser, previous, index)
        }
    }
}
