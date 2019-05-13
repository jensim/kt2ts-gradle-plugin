build.kotlin.kts
```kotlin
plugins {
    id("se.jensim.kt2ts") version "0.1.0-SNAPSHOT"
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

# Dev

```bash
./gradlew -b dev.build.gradle.kts :kt2ts
```
