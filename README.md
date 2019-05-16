# Kotlin to TypeScript

Have your backend declare the data types (DTOs) for its API, and use em in the front end.
Great when working with Angular or other TypeScript frameworks.

This project is a wrapper around the [ts-generator](https://github.com/ntrrgc/ts-generator) project

[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/se/jensim/kt2ts/se.jensim.kt2ts.gradle.plugin/maven-metadata.xml.svg?colorB=007ec6&label=gradle-plugin)](https://plugins.gradle.org/plugin/se.jensim.kt2ts)
[![Build Status](https://travis-ci.org/jensim/kt2ts-gradle-plugin.svg?branch=master)](https://travis-ci.org/jensim/kt2ts-gradle-plugin)

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

## Dependencies and license stuff
| Dependency                                                             | License                                                                                                                        |
|------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| kt2ts (this)                                                           | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)           |
| [ts-generator](https://github.com/ntrrgc/ts-generator)                 | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)           |
| [reflections](https://github.com/ronmamo/reflections)                  | [![WTFPL](https://img.shields.io/badge/license-WTFPL-orange.svg)](http://www.wtfpl.net/)                                                                                                 |
| [javassist](https://github.com/jboss-javassist/javassist) (transitive) | [![License: MPL 2.0](https://img.shields.io/badge/License-MPL%202.0-brightgreen.svg)](https://opensource.org/licenses/MPL-2.0) |                                   
| [guava](https://github.com/google/guava) (transitive)                  | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)           |                                   

