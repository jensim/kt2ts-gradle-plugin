package se.jensim.gradle.plugin.kt2ts.internal

import org.junit.Test

class Kt2TsExceptionTest {

    @Test(expected = Kt2TsException::class)
    fun `throws like a rock`() {
        throw Kt2TsException("Hi- ooooooo")
    }

    @Test(expected = Kt2TsException::class)
    fun `wraps like a paper`() {
        throw Kt2TsException("Moooo", RuntimeException("Gooo"))
    }
}
