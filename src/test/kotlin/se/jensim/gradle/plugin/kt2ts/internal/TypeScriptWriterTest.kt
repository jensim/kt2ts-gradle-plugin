package se.jensim.gradle.plugin.kt2ts.internal

import org.hamcrest.Matchers.emptyString
import org.hamcrest.Matchers.not
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import se.jensim.gradle.plugin.kt2ts.internal.classfilefindertest.ClassToFind
import java.io.File

class TypeScriptWriterTest {

    @JvmField
    @Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun `write typescript to file`() {
        val destination = File(tempFolder.root, "output.d.ts")
        assertFalse(destination.exists())

        TypeScriptWriter(destination).write(setOf(ClassToFind::class))

        assertTrue(destination.exists())
        assertThat(destination.readText(), not(emptyString()))
    }
}
