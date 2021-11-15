# primeCombinator

General purpose parsing framework. Simplify parsing of text. Allows capture complex nested formats with simple and
human-readable syntax.

## How to use:

Let's say we need to fetch protocol and domain name from URL.
Here is an example how we take it from url: "http://combinator.primeframeworks.com"

```
//Kotlin example

val parsedUrl = SequenceOf(
    Any(Str("http"), Str("https")),
    Str("://"),
    CustomWord(EnglishLetter().asChar(), Character('.')))
    .parse("http://combinator.primeframeworks.com")
    .get()

val protocol = (Any().fromSequence(parsedUrl.sequence, 0).anyOne as Str.StrParsed).str
val domainName = CustomWord().fromSequence(parsedUrl.sequence, 2).customWord

assertEquals("http", protocol)
assertEquals("combinator.primeframeworks.com", domainName)
```
Framework does its best to provide strictly typed system where possible as well as convenient API.

## Documentation:
http://combinator.primeframeworks.com 

## How to install:
Maven:
```xml
<dependency>
    <groupId>com.primeframeworks</groupId>
    <artifactId>primeCombinator</artifactId>
    <version>1.0.1</version>
    <scope>compile</scope>
</dependency>
```

Gradle:
```groovy
implementation com.primeframeworks:primeCombinator:1.0.1
```

## Licence:
Apache License
Version 2.0

## Contacts:
#### Author: Roman Fantaev
#### Email: fantaevroman@gmail.com