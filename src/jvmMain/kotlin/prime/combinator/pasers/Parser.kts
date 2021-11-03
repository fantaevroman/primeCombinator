interface Parser {
    fun getType(): String
    fun parse(context: ParsingContext): ParsingContext
    fun map(transformer: (from: ParsingContext) -> ParsingContext) = map(this, transformer)
    fun mapFail(transformer: (from: ParsingContext) -> ParsingContext) = mapFail(this, transformer)
}

fun mapFail(self: Parser, transformer: (from: ParsingContext) -> ParsingContext) = object : Parser {
    override fun getType() = self.getType()
    override fun parse(context: ParsingContext): ParsingContext {
        val selfParsed = self.parse(context)
        return if (selfParsed.fail()) {
            transformer(selfParsed)
        } else {
            selfParsed
        }
    }
}

fun map(self: Parser, transformer: (from: ParsingContext) -> ParsingContext) = object : Parser {
    override fun getType() = self.getType()
    override fun parse(context: ParsingContext): ParsingContext {
        val selfParsed = self.parse(context)
        return if (selfParsed.fail()) {
            selfParsed
        } else {
            transformer(selfParsed)
        }
    }
}
