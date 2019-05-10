package com.example

import se.jensim.gradle.plugin.kt2ts.Kotlin2Typescript

@Kotlin2Typescript
data class OneDataType(val types: List<ThreeDataType>, val e: FouthEnum)

data class TwoDataType(val name: String)

data class ThreeDataType(val name: String)

enum class FouthEnum {
    FOUR
}
