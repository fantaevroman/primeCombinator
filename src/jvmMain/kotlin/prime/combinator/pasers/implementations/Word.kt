package prime.combinator.pasers.implementations

import prime.combinator.pasers.Parsed
import prime.combinator.pasers.Parser
import prime.combinator.pasers.ParsingError
import java.util.*

class Word() : Parser<Word.WordParsed> {
    inner class WordParsed(
        val parsed: Parsed,
        val word: String,
        error: Optional<ParsingError> = Optional.empty()
    ) :
        Parsed(
            parsed.text,
            parsed.currentIndex(),
            parsed.currentIndex() + 1,
            error.map { emptyMap<String, String>() }.orElseGet { hashMapOf(Pair(getType(), word)) },
            getType(),
            error
        )


    override fun getType() = "Word"

    override fun parse(parsed: Parsed): WordParsed {
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