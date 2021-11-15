plugins {
    kotlin("multiplatform") version "1.4.31"
    id("maven-publish")
    id("signing")
}

group = "com.primeframeworks"
version = "1.0.1"

val sonotypeUsername: String by project
val sonotypePassword: String by project

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
        withJava()
    }
}

publishing {
    publications {
        create<MavenPublication>("primeCombinatorLibrary") {
            pom {
                name.set("Prime Combinator Library")
                description.set("Kotlin parsing combinator library which allows to write human-readable parsing code.")
                url.set("http://combinator.primeframeworks.com")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("treox")
                        name.set("Roman Fantaev")
                        email.set("fantaevroman@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/fantaevroman/primeCombinator.git")
                    developerConnection.set("scm:git:ssh://github.com:fantaevroman/primeCombinator.git")
                    url.set("https://github.com/fantaevroman/primeCombinator.git")
                }
            }
            from(components["kotlin"])
        }
    }

    repositories {
/**
        maven {
            name = "localRepo"
            url = uri(layout.buildDirectory.dir("repo"))
        }
 **/

        maven {
            name = "centralStageRepo"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = sonotypeUsername
                password = sonotypePassword
            }
        }


    }
}

signing {
    sign(publishing.publications)
}

tasks.withType<PublishToMavenRepository>().configureEach {
    onlyIf {
        (publication.artifactId == "primeCombinator")
    }
}