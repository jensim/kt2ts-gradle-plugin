# Kotlin to TypeScript

Have your backend declare the data types (DTOs) for its API, and use em in the front end.
Great when working with Angular or other TypeScript frameworks.

## Usage
build.kotlin.kts
```kotlin
plugins {
    id("se.jensim.kt2ts") version "0.3.0"
}

kt2ts {
    annotation = "com.example.ToTypescript"
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
