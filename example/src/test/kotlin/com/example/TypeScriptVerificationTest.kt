package com.example

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TypeScriptVerificationTest {

    @Test
    fun `verify that a typescript file has been created and is not empty`() {
        val file = File("build/ts/kt2ts.d.ts")
        val exists = file.exists()

        assertTrue(exists)

        val content = file.readText()

        assertTrue(content.contains("OneDataType"))
        assertFalse(content.contains("TwoDataType"))
        assertTrue(content.contains("ThreeDataType"))
        assertTrue(content.contains("FouthEnum"))
        assertTrue(content.contains("JavaClassTypes"))
    }

    @Test
    fun `verify that a java-only typescript file has been created and is not empty`() {
        val file = File("build/ts/java-only.d.ts")
        val exists = file.exists()

        assertTrue(exists)

        val content = file.readText()

        assertFalse(content.contains("OneDataType"))
        assertFalse(content.contains("TwoDataType"))
        assertFalse(content.contains("ThreeDataType"))
        assertFalse(content.contains("FouthEnum"))
        assertTrue(content.contains("JavaClassTypes"))
    }
}
