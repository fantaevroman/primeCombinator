package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import prime.combinator.pasers.implementations.SequenceOf.SequenceOfParsed
import java.util.*

open class SequenceOf(vararg val parsers: Parser<out Parsed>) : Parser<SequenceOfParsed> {

    inner class SequenceOfParsed(
        val parsed: Parsed,
        val sequence: List<Parsed>,
        error: Optional<ParsingError> = Optional.empty()
    ) : Parsed(
        parsed.text,
        sequence.first().indexStart,
        sequence.last().indexEnd,
        error.map { emptyMap<String, List<Parsed>>() }.orElseGet {
            hashMapOf(Pair(getType(), sequence))
        },
        getType(),
        error
    )

    override fun getType() = "SequenceOf"

    override fun parse(parsed: Parsed): SequenceOfParsed {
        val successSequence = mutableListOf<Parsed>()
        val parsersIterator = parsers.iterator().withIndex()
        var currentContext = parsed

        while (parsersIterator.hasNext()) {
            val nextParser = parsersIterator.next()
            val nextTransformedParser =
                oneEachTransform(nextParser.index, currentContext, nextParser.value, successSequence)

            currentContext = nextTransformedParser.parse(currentContext)
            if (currentContext.success()) {
                successSequence.add(currentContext)
            } else {
                SequenceOfParsed(parsed, successSequence, parsed.error)
            }
        }

        return SequenceOfParsed(successSequence.last(), successSequence)
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
            currentContext: Parsed,
            currentParser: Parser<out Parsed>,
            previous: List<Parsed>,
            currentIndex: Int
        ) -> Parser<out Parsed>
    ) = object : SequenceOf(*this.parsers) {
        override fun getType(): String {
            return super.getType()
        }

        override fun parse(parsed: Parsed): SequenceOfParsed {
            return super.parse(parsed)
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
