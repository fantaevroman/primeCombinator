package prime.combinator.pasers


interface Parser<T : Parsed> {
    fun getType(): String
    fun parse(parsed: Parsed): T

    fun <A : Parsed> map(
        transformerSuccess: (from: T) -> A,
        transformerFail: (from: T) -> A
    ): Parser<A> {
        val type = getType()
        val selfParse = { context: Parsed -> parse(context) }

        return object : Parser<A> {
            override fun getType() = type
            override fun parse(parsed: Parsed): A {
                val selfParsed = selfParse(parsed)
                return if (selfParsed.success()) {
                    transformerSuccess(selfParsed)
                } else {
                    transformerFail(selfParsed)
                }
            }
        }
    }
}
