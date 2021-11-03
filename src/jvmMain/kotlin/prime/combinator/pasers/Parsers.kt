package prime.combinator.pasers

import prime.combinator.Parser
import prime.combinator.ParsingContext
import prime.combinator.ParsingError
import java.util.*

fun createContext(text: String) = ParsingContext(text, -1, -1, emptyMap(), "empty", Optional.empty())

abstract class EndOfInputParser : Parser {
    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        return if (context.text.length - 1 < currentIndex) {
            context.copy(
                error = Optional.of(ParsingError("Can't parse at index:[${currentIndex}] end of text")),
                type = getType(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = emptyMap()
            )
        } else {
            parseNext(context)
        }
    }

    abstract fun parseNext(context: ParsingContext): ParsingContext
}

class Long : EndOfInputParser() {
    override fun getType() = "Long"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val scanner = Scanner(context.text)
        val currentIndex = context.indexEnd + 1

        return if (scanner.hasNextLong()) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(
                    Pair("longValue", scanner.nextInt())
                )
            )
        } else {
            context.copy(
                error = Optional.of(ParsingError("Can't parse Long at index:[${currentIndex}]")),
                type = "Long",
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = emptyMap()
            )
        }
    }
}

class AnyCharacter : EndOfInputParser() {
    override fun getType() = "AnyCharacter"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        return context.copy(
            indexStart = currentIndex,
            indexEnd = currentIndex,
            context = hashMapOf(Pair("anyCharacter", context.text[currentIndex.toInt()])),
            type = getType()
        )
    }

    companion object {
        fun join(
            list: List<ParsingContext>,
            separator: String = "",
            contextBodyName: String = "anyCharacter"
        ): ParsingContext {
            return if (list.isEmpty()) {
                return ParsingContext("empty join Str", 0, 0, emptyMap(), "emptyJoin", Optional.empty())
            } else {
                list.last().copy(
                    type = "Str",
                    context = hashMapOf(
                        Pair(
                            "str",
                            list.map { anyCharContext -> anyCharContext.context[contextBodyName] }
                                .joinToString(separator = separator)
                        )
                    )
                )
            }
        }
    }

}

open class Str(val string: String) : Parser {
    override fun getType() = "Str"

    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        if (context.text.length - 1 < currentIndex - 1 + string.length) {
            return context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex + string.length - 1,
                error = Optional.of(ParsingError("Can't parse at index:[${currentIndex}] end of text")),
                type = "Str",
                context = emptyMap()
            )
        } else {
            val expectedIndex = currentIndex.toInt()
            val indexOf = context.text.indexOf(string, expectedIndex)
            return if (indexOf == expectedIndex) {
                context.copy(
                    indexStart = currentIndex,
                    indexEnd = currentIndex + string.length - 1,
                    context = hashMapOf(
                        Pair("str", string)
                    ),
                    type = "Str"
                )
            } else {
                context.copy(
                    indexStart = currentIndex,
                    indexEnd = currentIndex + string.length - 1,
                    error = Optional.of(ParsingError("Can't parse at index:[${currentIndex}] [$string] not found")),
                    type = "Str",
                    context = emptyMap()
                )
            }
        }
    }
}

class DoubleQuote() : Str(""""""")

class Spaces() : Parser {
    override fun getType() = "Spaces"
    override fun parse(context: ParsingContext): ParsingContext =
        RepeatUntil(Character(' '), Not(Character(' ')))
            .joinRepeaters {
                AnyCharacter.join(it, "", "space")
            }
            .map {
                it.copy(
                    type = "Spaces",
                    context = hashMapOf(
                        Pair(
                            "spaces",
                            (it.context["repeater"] as ParsingContext).context["str"] as String
                        )
                    )
                )
            }.parse(context)
}

open class RepeatAndJoin(
    val parser: Parser,
    val joinBetween: (contexts: List<ParsingContext>) -> ParsingContext
) : Parser {
    override fun getType() = "RepeatAndJoin"
    override fun parse(context: ParsingContext): ParsingContext =
        RepeatUntil(parser, Not(parser))
            .joinRepeaters {
                joinBetween(it)
            }
            .map {
                it.copy(
                    type = "RepeatAndJoin",
                    context = hashMapOf(
                        Pair(
                            "joint",
                            (it.context["repeater"] as ParsingContext).context["str"] as String
                        )
                    )
                )
            }.parse(context)
}

class CustomWord(
    vararg val parsers: Parser,
    val joinBetween: (contexts: List<ParsingContext>) -> ParsingContext = { AnyCharacter.join(it, "", "character") }
) : Parser {
    override fun getType() = "CustomWord"

    override fun parse(context: ParsingContext): ParsingContext {
        return RepeatAndJoin(Any(*parsers)) { joinBetween(it) }
            .map {
                it.copy(
                    type = getType(),
                    context = hashMapOf(
                        Pair(
                            "word",
                            it.context["joint"]!!
                        )
                    )
                )
            }.parse(context)
    }
}

class Word() : Parser {
    override fun getType() = "Word"

    override fun parse(context: ParsingContext): ParsingContext {
        return RepeatAndJoin(EnglishLetter()) { AnyCharacter.join(it, "", "letter") }
            .map {
                it.copy(
                    type = getType(),
                    context = hashMapOf(
                        Pair(
                            "word",
                            it.context["joint"]!!
                        )
                    )
                )
            }.parse(context)
    }
}

class EnglishDigit : EndOfInputParser() {
    override fun getType() = "EnglishLetter"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val next = Scanner(context.text).toString().toCharArray()[0]
        val currentIndex = context.indexEnd + 1
        return if (((next in '1'..'9'))) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("character", next)),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse English letter at index:[${currentIndex}], required:[1..9] but was:[$next]")
                ),
                type = getType(),
                context = emptyMap(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
            )
        }
    }
}

class EnglishLetter : EndOfInputParser() {
    override fun getType() = "EnglishLetter"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        val next = context.text.toCharArray()[currentIndex.toInt()]
        return if (((next in 'a'..'z')) || ((next in 'A'..'Z'))) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("letter", next)),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse English letter at index:[${currentIndex}], required:[a..z, A..Z] but was:[$next]")
                ),
                type = getType(),
                context = emptyMap(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
            )
        }
    }

    fun asChar(): Parser {
        return this.map { it.copy(
            type = "Character",
            context = hashMapOf(Pair("character", it.context["letter"].toString()))) }
    }
}

class Character(private val char: Char) : EndOfInputParser() {
    override fun getType() = "Character"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        val next = context.text.toCharArray()[currentIndex.toInt()]
        return if (next == char) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("character", next)),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse character at index:[${currentIndex}], required:[$char] but was:[$next]")
                ),
                type = getType(),
                context = emptyMap(),
                indexStart = currentIndex,
                indexEnd = currentIndex,
            )
        }
    }
}

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

class RepeatableBetween(
    private val left: Parser,
    private val between: Parser,
    private val right: Parser,
    private val sequenceOf: Parser = SequenceOf(left, RepeatUntil(between, right), right)
) : Parser {
    override fun getType() = "RepeatableBetween"

    override fun parse(context: ParsingContext): ParsingContext {
        return sequenceOf.map {
            val between = (it.context["sequence"] as List<ParsingContext>)[1]
            val repeaters = (between.context["repeaters"] as List<ParsingContext>)
            it.copy(
                context = hashMapOf(
                    Pair("between", repeaters),
                    Pair("left", (it.context["sequence"] as List<ParsingContext>)[0]),
                    Pair("right", (it.context["sequence"] as List<ParsingContext>)[2])
                )
            )
        }.parse(context).copy(
            type = getType(),
            indexStart = context.indexStart + 1
        )
    }

    fun mapEach(
        transformer: (currentContext: ParsingContext, currentParser: Parser, previous: List<ParsingContext>, currentIndex: Int) -> Parser
    ) = RepeatableBetween(left, between, right,
        SequenceOf(left, RepeatUntil(between, right), right)
            .mapEach { currentContext, currentParser, previous, currentIndex ->
                transformer(currentContext, currentParser, previous, currentIndex)
            }
    )


    fun joinBetween(joinBetween: (contexts: List<ParsingContext>) -> ParsingContext): Parser {
        return this.map { original ->
            original.copy(
                context =
                hashMapOf(
                    Pair("left", original.context["left"]!!),
                    Pair("right", original.context["right"]!!),
                    Pair(
                        "between",
                        joinBetween(original.context["between"] as List<ParsingContext>)
                    )
                )
            )
        }
    }

    fun joinBetweenCharsToStrings() = this.map { repeatableContext ->
        val between = repeatableContext.context["between"] as List<ParsingContext>
        fun isString(c: ParsingContext) = c.type == "AnyCharacter" || c.type == "Str"
        val joined = mutableListOf<ParsingContext>()

        for (r in between) {
            if (joined.isEmpty()) {
                joined.add(r)
                continue
            } else {
                val l = joined.last()
                if (isString(l) && isString(r)) {
                    joined.removeLast()
                    joined.add(
                        l.copy(
                            type = "Str",
                            context = hashMapOf(
                                Pair(
                                    "str",
                                    l.context[if (l.type == "AnyCharacter") "anyCharacter" else "str"].toString() + r.context["anyCharacter"]
                                )
                            )
                        )
                    )
                } else {
                    joined.add(r)
                }
            }
        }
        repeatableContext.copy(context = mapOf(Pair("between", joined)))
    }
}

class Not(
    private val parser: Parser
) : Parser {
    override fun getType() = "Not(" + parser.getType() + ")"

    override fun parse(context: ParsingContext): ParsingContext {
        val parsedContext = parser.parse(context)
        return if (parsedContext.success()) {
            parsedContext.copy(
                type = getType(),
                error = Optional.of(ParsingError("Expected not ${parser.getType()} but was it"))
            )
        } else {
            parsedContext.copy(
                type = getType(),
                error = Optional.empty()
            )
        }

    }
}

class Between(
    private val left: Parser,
    private val between: Parser,
    private val right: Parser,
) : Parser {
    override fun getType() = "Between"

    override fun parse(context: ParsingContext): ParsingContext {
        return SequenceOf(left, between, right).parse(context).copy(
            type = getType()
        )
    }
}

class Beginning() : Parser {
    override fun getType() = "Beginning"

    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexStart
        return if (currentIndex.toInt() == -1) {
            context.copy(
                indexStart = -1,
                indexEnd = -1,
                context = hashMapOf(Pair("beginning", "reached")),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse character at index:[${currentIndex}], required:[beginning] but current index[$currentIndex]")
                ),
                type = "Beginning",
                context = emptyMap()
            )
        }
    }
}


class End() : Parser {
    override fun getType() = "End"

    override fun parse(context: ParsingContext): ParsingContext {
        val currentIndex = context.indexEnd + 1
        return if (context.text.length - 1 < currentIndex) {
            context.copy(
                indexStart = currentIndex,
                indexEnd = currentIndex,
                context = hashMapOf(Pair("end", "reached")),
                type = getType()
            )
        } else {
            context.copy(
                error = Optional.of(
                    ParsingError("Can't parse character at index:[${currentIndex}], required:[end] but has more characters")
                ),
                type = "End",
                context = emptyMap()
            )
        }
    }
}

class Any(private vararg val parsers: Parser) : EndOfInputParser() {
    override fun getType() = "Any"

    override fun parseNext(context: ParsingContext): ParsingContext {
        val iterator = parsers.iterator()
        while (iterator.hasNext()) {
            val parserResult = iterator.next().parse(context)
            if (parserResult.success()) {
                return parserResult
            }
        }

        return context.copy(
            error = Optional.of(
                ParsingError(
                    "Non of supplied parsers matched:[${parsers.joinToString(separator = ",") { it.getType() }}]"
                )
            )
        )
    }
}

class RepeatUntil(
    private val repeater: Parser,
    private val until: Parser
) : Parser {
    override fun getType() = "RepeatUntil"

    override fun parse(context: ParsingContext): ParsingContext {
        val repeaters = mutableListOf(repeater.parse(context))
        val untils = mutableListOf<ParsingContext>()

        while (true) {
            if (repeaters.last().fail()) {
                return repeaters.last().copy(
                    context = hashMapOf(Pair("repeaters", repeaters)),
                    type = "RepeatUntil"
                )
            }

            val currentUntil = until.parse(repeaters.last())
            if (currentUntil.success()) {
                untils.add(currentUntil)
                return repeaters.last().copy(
                    context = hashMapOf(
                        Pair("repeaters", repeaters),
                        Pair("untils", untils)
                    ),
                    type = "RepeatUntil"
                )
            }

            repeaters.add(repeater.parse(repeaters.last()))
        }
    }

    fun joinRepeaters(joinBetween: (contexts: List<ParsingContext>) -> ParsingContext): Parser {
        return this.map { original ->
            original.copy(
                context = hashMapOf(
                    Pair(
                        "repeater",
                        joinBetween(original.context["repeaters"] as List<ParsingContext>)
                    )
                )
            )
        }
    }
}




