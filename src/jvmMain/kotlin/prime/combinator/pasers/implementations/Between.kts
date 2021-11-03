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