package se.jensim.gradle.plugin.kt2ts

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import se.jensim.gradle.plugin.kt2ts.internal.Kt2TsException
import se.jensim.gradle.plugin.kt2ts.internal.Kt2TsService
import java.io.File
import kotlin.test.assertFailsWith

class Kt2TsTaskTest {

    @JvmField
    @Rule
    val tempDir = TemporaryFolder()

    private lateinit var service: Kt2TsService
    private lateinit var project: Project
    private lateinit var task: Kt2TsTask
    private lateinit var extension: Kt2TsPluginExtension

    @Before
    fun setUp() {
        service = mock()
        project = ProjectBuilder.builder()
            .withProjectDir(tempDir.root)
            .build()
        task = project.tasks.create("kt2ts", Kt2TsTask::class.java).apply {
            overrideService = service
        }
        extension = project.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)

    }


    @Test
    fun `instantiate class`() {
        task.generateTypescript()

        verify(service).generateTypescript(extension)
    }

    @Test
    fun getSource() {
        extension.classFilesSources {
            this.classesDirs = project.files("/")

            val source = task.getSourceFiles()

            Assert.assertThat(source, Matchers.hasSize(1))
        }
    }

    @Test
    fun `getSource throws exception if not configured`() {
        assertFailsWith<Kt2TsException> {
            task.getSourceFiles()
        }
    }

    @Test
    fun getOutput() {
        extension.generationSpecification {
            this.annotations = listOf("com.example.Annotation")
            this.outputFile = File("/")
        }

        val outputFiles = task.getOutput()

        Assert.assertThat(outputFiles, Matchers.hasSize(1))
    }

    @Test
    fun `getOutput fails if not specified`() {
        assertFailsWith<Kt2TsException> {
            task.getOutput()
        }
    }

    annotation class MyAnnotation
    class FoundClass
}

