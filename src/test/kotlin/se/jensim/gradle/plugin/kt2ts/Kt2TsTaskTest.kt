package se.jensim.gradle.plugin.kt2ts

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.ArgumentMatchers.anyString
import se.jensim.gradle.plugin.kt2ts.internal.ClassFinder
import se.jensim.gradle.plugin.kt2ts.internal.Kt2TsException
import se.jensim.gradle.plugin.kt2ts.internal.TypeScriptWriter
import java.io.File
import kotlin.test.assertFailsWith

class Kt2TsTaskTest {

    @JvmField
    @Rule
    val tempDir = TemporaryFolder()
    private val myClasses = setOf(FoundClass::class)
    private val classFinder: ClassFinder = mock {
        on { getAnnotatedClasses(anyString()) } doReturn myClasses
    }
    private val typeScriptWriter: TypeScriptWriter = mock()

    @Test
    fun `instanciate class`() {
        val outFile = File(tempDir.root, "build/ts/kt2ts.d.ts")
        val task = setUp(
            annotation = "se.jensim.gradle.plugin.kt2ts.Kt2TsTaskTest${"$"}MyAnnotation",
            classesDirs = "build/classes/kotlin/test/se/jensim",
            outputFile = outFile
        )
        task.generateTypescript()

        verify(typeScriptWriter).write(myClasses)
    }

    @Test
    fun `no source`() {
        val task = setUp(
            annotation = "se.jensim.gradle.plugin.kt2ts.Kt2TsTaskTest${"$"}MyAnnotation",
            classesDirs = null,
            outputFile = File(tempDir.root, "build/ts/kt2ts.d.ts")
        )

        assertFailsWith<Kt2TsException> {
            task.generateTypescript()
        }
    }

    @Test
    fun `no output`() {
        val task = setUp(
            annotation = "se.jensim.gradle.plugin.kt2ts.Kt2TsTaskTest${"$"}MyAnnotation",
            classesDirs = "build/classes/kotlin/test/se/jensim",
            outputFile = null
        )

        assertFailsWith<Kt2TsException> {
            task.generateTypescript()
        }
    }

    @Test
    fun `no annotation`() {
        val task = setUp(
            annotation = null,
            classesDirs = "build/classes/kotlin/test/se/jensim",
            outputFile = File(tempDir.root, "build/ts/kt2ts.d.ts")
        )

        assertFailsWith<NullPointerException> {
            task.generateTypescript()
        }
    }

    fun setUp(annotation: String? = null, classesDirs: String? = null, outputFile: File? = null): Kt2TsTask {
        val project = ProjectBuilder.builder()
            .withProjectDir(tempDir.root)
            .build()
        val task = project.tasks.create("kt2ts", Kt2TsTask::class.java)
        task.classfinderFactory = { classFinder }
        task.typeScriptWriterFactory = { typeScriptWriter }
        val extension = project.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)
        extension.annotation = annotation
        extension.classesDirs = classesDirs?.run { project.files(this) }
        extension.outputFile = outputFile?.run { project.file(this) }
        return task
    }

    annotation class MyAnnotation
    class FoundClass
}

