package prime.combinator.pasers


interface Parser<S : Parsed> {
    fun parse(previous: Parsed): ParsedResult<S>

    fun <A : Parsed> map(
        transformer: (from: S) -> A
    ): Parser<A> {
        val selfParse = { context: Parsed -> parse(context) }

        return object : Parser<A> {
            override fun parse(previous: Parsed): ParsedResult<A> {
                val selfParsed = selfParse(previous)
                return if (selfParsed.success()) {
                    ParsedResult.asSuccess(transformer(selfParsed.parsed.get()))
                } else {
                    ParsedResult.asError(selfParsed.error.get())
                }
            }
        }
    }
}

