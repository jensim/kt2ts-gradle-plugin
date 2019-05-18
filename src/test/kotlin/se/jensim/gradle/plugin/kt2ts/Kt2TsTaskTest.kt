package se.jensim.gradle.plugin.kt2ts

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class Kt2TsTaskTest{

    @JvmField
    @Rule
    val tempDir = TemporaryFolder()

    @Test
    fun `instanciate class`() {
        val project = ProjectBuilder.builder()
            .withProjectDir(tempDir.root)
            .build()
        val task = project.tasks.create("kt2ts", Kt2TsTask::class.java)
        val extension = project.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)
        val annotation = "se.jensim.gradle.plugin.kt2ts.Kt2TsTaskTest${"$"}MyAnnotation"
        val classesDirs = project.files("build/classes/kotlin/test/se/jensim")
        val outputFile = File(tempDir.root, "build/ts/kt2ts.d.ts")
        extension.annotation = annotation
        extension.classesDirs = classesDirs
        extension.outputFile = outputFile
        assertFalse(outputFile.exists())

        task.generateTypescript()
    }

    annotation class MyAnnotation
}

