package com.example

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TypeScriptVerificationTest {

    @Test
    fun `verify that a typescript file has been created and is not empty`() {
        val resource = javaClass.classLoader.getResource("/build/ts/kt2ts.d.ts")
        val toURI = resource.toURI()
        val file = File(toURI)
        val exists = file.exists()

        assertTrue(exists)

        val content = file.readText()

        assertTrue(content.contains("OneDataType"))
        assertFalse(content.contains("TwoDataType"))
        assertTrue(content.contains("ThreeDataType"))
        assertTrue(content.contains("FouthEnum"))
    }
}
