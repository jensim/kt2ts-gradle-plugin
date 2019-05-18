import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        jcenter()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.0.0")
    }
}
plugins {
    kotlin("jvm") version "1.3.31"
    `maven-publish`
    `java-gradle-plugin`
    id("org.sonarqube") version "2.7.1"
    id("com.gradle.plugin-publish") version "0.10.0"
    jacoco
}
repositories {
    mavenCentral()
    maven("https://jitpack.io")
    jcenter()
}

group = "se.jensim.kt2ts"
version = findProperty("releaseVersion") ?: "DEV"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    compileOnly("com.github.ntrrgc:ts-generator:1.1.1") {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation("org.reflections:reflections:0.9.11")

    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("reflect"))
    testImplementation("com.github.ntrrgc:ts-generator:1.1.1")
    testImplementation("org.hamcrest:hamcrest-core:2.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
}

tasks.withType<Jar> {
    val deps = listOf("ts-generator")
        .joinToString("|", "^(", ")$") { "$it.*.jar" }
    configurations["compileClasspath"]
        .filter { it.name.matches(Regex(deps)) }
        .forEach { file: File ->
            from(zipTree(file.absoluteFile))
        }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

pluginBundle {
    description = """
Generate TypeScript Definitions from your existing code.

In case you have your backend written in kotlin or java and you dont want to write your Angular type definitions yourself.

https://github.com/jensim/kt2ts-gradle-plugin/blob/${project.version}/README.md
"""
    website = "https://github.com/jensim/kt2ts-gradle-plugin"
    vcsUrl = "https://github.com/jensim/kt2ts-gradle-plugin"
    tags = listOf("kotlin", "typescript", "kt2ts", "kt2js", "generate", "definitions", "ts-generator", "angular", "ts", "kt", "transpile")
}

gradlePlugin {
    plugins {
        create("kt2ts") {
            id = "se.jensim.kt2ts"
            version = project.version
            displayName = "Kotlin2TypeScript"
            implementationClass = "se.jensim.gradle.plugin.kt2ts.Kt2TsPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.destination = file("$buildDir/jacocoHtml")
    }
}
