# Kotlin to TypeScript

Have your backend declare the data types (DTOs) for its API, and use em in the front end.
Great when working with Angular or other TypeScript frameworks.

[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/se/jensim/kt2ts/se.jensim.kt2ts.gradle.plugin/maven-metadata.xml.svg?colorB=007ec6&label=gradle-plugin)](https://plugins.gradle.org/plugin/se.jensim.kt2ts)

## Usage
build.kotlin.kts
```kotlin
plugins {
    id("se.jensim.kt2ts") version "0.4.0"
}

kt2ts {
    annotation = "com.example.ToTypescript"
    classesDirs = files(
            tasks.findByName("compileKotlin")?.outputs,
            tasks.findByName("compileJava")?.outputs)
    outputFile = file("$buildDir/ts/kt2ts.d.ts")
}
```

Your custom annotation and API data classes
```kotlin
package com.example

annotation class ToTypescript

@ToTypescript
data class OneDataType(val types: List<ThreeDataType>, val e: FouthEnum)

data class TwoDataType(val name: String)

data class ThreeDataType(val name: String)

enum class FouthEnum {
    FOUR
}

```
