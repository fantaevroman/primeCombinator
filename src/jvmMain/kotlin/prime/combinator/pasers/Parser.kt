package prime.combinator.pasers

/**
 * Parser allows parsing text.
 * It requires parsing result from previous parser.
 * In case we want to start parsing and there is no previous parser we can use
 * this function: startParsing(text: String) - which is static function which create initial
 * parsing result which then can be used by any other parser.
 * Example: Str("Name").parse(startParsing("Name is ...")).get()
 * Here we created parsing result from text and forwarded it to parser "Str".
 * Parser always goes forward in text and increase indexes in result.
 * startParsing("Name is ...") creates Parsed with indexEnd:0, after we pass it to
 * Str("Name") and parse it returns Parsed  with indexEnd:3 which allows forwarding it to next parser
 * which will start from position 4 and start parsing same text further.
 *
 * The conception is borrowed from "Parsing Combinator" pattern.
 *
 * @License: Apache-2.0
 * @source: https://github.com/fantaevroman/primeCombinator
 * @author: Roman Fantaev
 * @contact: FantaevRoman@gmail.com
 * @since 2021
 */
interface Parser<S : Parsed> {
    fun parse(previous: Parsed): ParsedResult<S>
    fun parse(str: String): ParsedResult<S>{
        return parse(startParsing(str))
    }

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

    fun fromSequence(sequence: List<Parsed>, index: Int): S {
        return sequence[index] as S
    }

}

