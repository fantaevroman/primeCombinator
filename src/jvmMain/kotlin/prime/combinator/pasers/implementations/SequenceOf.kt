package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingContext

open class SequenceOf(
    vararg val parsers: Parser
) : Parser {
    override fun getType() = "SequenceOf"

    override fun parse(context: ParsingContext): ParsingContext {
        val successSequence = mutableListOf<ParsingContext>()
        val parsersIterator = parsers.iterator().withIndex()
        var currentContext = context

        while (parsersIterator.hasNext()) {
            val nextParser = parsersIterator.next()
            val nextTransformedParser =
                oneEachTransform(nextParser.index, currentContext, nextParser.value, successSequence)

            currentContext = nextTransformedParser.parse(currentContext)
            if (currentContext.success()) {
                successSequence.add(currentContext)
            } else {
                return currentContext.copy(
                    type = getType(),
                    context = hashMapOf(Pair("sequence", successSequence))
                )
            }
        }

        return successSequence.last().copy(
            type = getType(),
            context = hashMapOf(Pair("sequence", successSequence))
        )
    }

    open fun oneEachTransform(
        index: Int,
        currentContext: ParsingContext,
        currentParser: Parser,
        previous: List<ParsingContext>,
    ): Parser {
        return currentParser
    }

    fun mapEach(
        transformer: (
            currentContext: ParsingContext,
            currentParser: Parser,
            previous: List<ParsingContext>,
            currentIndex: Int
        ) -> Parser
    ) = object : SequenceOf(*this.parsers) {
        override fun getType(): String {
            return super.getType()
        }

        override fun parse(context: ParsingContext): ParsingContext {
            return super.parse(context)
        }

        override fun oneEachTransform(
            index: Int,
            currentContext: ParsingContext,
            currentParser: Parser,
            previous: List<ParsingContext>,
        ): Parser {
            return transformer(currentContext, currentParser, previous, index)
        }
    }
}
