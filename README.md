# Kotlin to TypeScript gradle-plugin

Have your backend declare the data types (DTOs) for its API, and use em in the front end.
Great when working with Angular or other TypeScript frameworks.

This project is a wrapper around the [ts-generator](https://github.com/ntrrgc/ts-generator) project

[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/se/jensim/kt2ts/se.jensim.kt2ts.gradle.plugin/maven-metadata.xml.svg?colorB=007ec6&label=gradle-plugin)](https://plugins.gradle.org/plugin/se.jensim.kt2ts)
[![Build Status](https://travis-ci.org/jensim/kt2ts-gradle-plugin.svg?branch=master)](https://travis-ci.org/jensim/kt2ts-gradle-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=se.jensim.kt2ts%3Akt2ts-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=se.jensim.kt2ts%3Akt2ts-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=se.jensim.kt2ts%3Akt2ts-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=se.jensim.kt2ts%3Akt2ts-plugin)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=se.jensim.kt2ts%3Akt2ts-plugin&metric=ncloc)](https://sonarcloud.io/dashboard?id=se.jensim.kt2ts%3Akt2ts-plugin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=se.jensim.kt2ts%3Akt2ts-plugin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=se.jensim.kt2ts%3Akt2ts-plugin)

## Usage
build.kotlin.kts
```gradle
plugins {
    id("se.jensim.kt2ts") version "$pluginVersion"
}

kt2ts {
    // Repeatable block for linking outputfile to a set of annotations
    output {
        outputFile = file("$buildDir/ts/kt2ts.d.ts")
        annotations = listOf("com.example.ToTypescript")
    }
    classFilesSources {
        // Two ways of setting classes dir, if both are set, both are jointly used
        // One has to be provided (for task input resolution to work properly, I made it mandatory)
        compileTasks = listOf(tasks.compileKotlin, tasks.compileJava)
        classesDirs = files("$buildDir/classes/kotlin/main")
    }
}
```

```gradle
// Shorthand versions - with default values
kt2ts {
    annotation = "com.example.ToTypescript"
    classFilesSources.compileTasks = listOf(tasks.compileKotlin, tasks.compileJava)
}
kt2ts {
    annotations = listOf("com.example.ToTypescript")
    classFilesSources.compileTasks = listOf(tasks.compileKotlin, tasks.compileJava)
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

## Dependencies and license stuff
| Dependency                                                             | License                                                                                                                        |
|------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| kt2ts (this)                                                           | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)           |
| [ts-generator](https://github.com/ntrrgc/ts-generator)                 | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)           |
| [reflections](https://github.com/ronmamo/reflections)                  | [![WTFPL](https://img.shields.io/badge/license-WTFPL-orange.svg)](http://www.wtfpl.net/)                                                                                                 |
| [javassist](https://github.com/jboss-javassist/javassist) (transitive) | [![License: MPL 2.0](https://img.shields.io/badge/License-MPL%202.0-brightgreen.svg)](https://opensource.org/licenses/MPL-2.0) |                                   
| [guava](https://github.com/google/guava) (transitive)                  | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)           |                                   

## Why?
Because we need something like kt2js
https://youtrack.jetbrains.com/issue/KT-16604

## Tests
We use jacoco for testcoverage of the plugin project, on top of that we build and test the example project that uses the plugin.
