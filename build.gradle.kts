plugins {
    kotlin("multiplatform") version "1.4.31"
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.4.32"
}

group = "com.primeframeworks"
version = "1.0.9"

val sonotypeUsername: String by project
val sonotypePassword: String by project

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

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
        publications.withType(MavenPublication::class) {
            artifact(javadocJar)
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