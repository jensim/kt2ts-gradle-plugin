package se.jensim.gradle.plugin.kt2ts

class Kt2TsException(message: String, e: Exception?) : RuntimeException(message, e) {
    constructor(message: String) : this(message, null)
}
