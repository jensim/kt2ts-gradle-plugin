package se.jensim.gradle.plugin.kt2ts

import com.nhaarman.mockitokotlin2.*
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test
import se.jensim.gradle.plugin.kt2ts.internal.Kt2TsException
import java.io.File
import kotlin.test.assertFailsWith

class Kt2TsPluginExtensionTest {

    @Test
    fun `kt2ts extension func`() {
        // given
        val project= ProjectBuilder.builder().build()
        project.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)

        // when
        project.kt2ts {
            classFilesSources {
                this.classesDirs = mock()
            }
            output {
                this.annotations = listOf("foo.bar")
                this.outputFile = File("./ts.d.ts")
            }
        }

        // then
        val annotation = project.extensions.findByType(Kt2TsPluginExtension::class.java)?.outputs?.firstOrNull()?.annotations?.firstOrNull()
        assertThat(annotation, equalTo("foo.bar"))
    }

    @Test
    fun `Kt2TsPluginExtension is mutable`() {
        val extension = Kt2TsPluginExtension()
        val settings: List<() -> Any?> =
            listOf(
                { extension.outputs.firstOrNull()?.outputFile },
                { extension.outputs.firstOrNull()?.annotations },
                { extension.classFilesSources.compileTasks },
                { extension.classFilesSources.classesDirs }
            )

        assertThat(settings.mapNotNull { it.invoke() }, hasSize(0))

        extension.output {
            outputFile = File("./")
            annotations = listOf("foo")
        }
        extension.classFilesSources {
            classesDirs = mock()
            compileTasks = mock()
        }

        assertThat(settings.mapNotNull { it.invoke() }, hasSize(4))
    }

    @Test
    fun `only annotation is required`() {
        val project = ProjectBuilder.builder().build()
        project.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)
        project.kt2ts.annotation = "foo.bar"

        assertThat(project.kt2ts.outputs.firstOrNull()?.outputFile, notNullValue())
        assertFailsWith<Kt2TsException> { project.kt2ts.classDirFiles }

        project.kt2ts.classFilesSources.classesDirs = project.files("./")
        val classDirFiles = project.kt2ts.classDirFiles
        assertThat(classDirFiles, hasSize(1))
        assertThat(classDirFiles.first(), notNullValue())

    }
}
