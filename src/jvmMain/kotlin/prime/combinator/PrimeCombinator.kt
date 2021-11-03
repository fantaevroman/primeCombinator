package prime.combinator

import java.util.*

data class ParsingError(val text: String)

data class ParsingContext(
  val text: String,
  val indexStart: Long,
  val indexEnd: Long,
  val context: Map<String, Any>,
  val type: String,
  val error: Optional<ParsingError>
) {
  fun fail() = error.isPresent
  fun success() = !error.isPresent
  override fun toString(): String {
    return "ParsingContext(type='$type', error=$error)"
  }
}

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
