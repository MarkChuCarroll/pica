plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    application
    antlr
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    antlr("org.antlr:antlr4:4.11.1") // use ANTLR version 4
    implementation("com.lectra:koson:1.2.4")
    implementation("com.github.ajalt.clikt:clikt:3.5.0")
    implementation("com.google.guava:guava:30.1.1-jre")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testImplementation(kotlin("test"))

}

application {
    // Define the main class for the application.
    mainClass.set("org.goodmath.pica.compiler.MainKt")
}

java.sourceSets["main"].java {
    srcDir(files("${buildDir}/generated-src/antlr").builtBy(tasks.generateGrammarSource))
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-long-messages")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
