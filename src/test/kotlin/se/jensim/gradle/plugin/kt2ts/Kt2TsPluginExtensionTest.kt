package se.jensim.gradle.plugin.kt2ts

import com.nhaarman.mockitokotlin2.*
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File

class Kt2TsPluginExtensionTest {

    @Test
    fun `kt2ts extension func`() {
        // given
        val mockExtension: ExtensionContainer = mock()
        val mockProject: Project = mock {
            on { extensions } doReturn mockExtension
        }

        // when
        mockProject.kt2ts {
            classFilesSources {
                this.classesDirs = mock()
            }
            generationSpecification {
                this.annotations = listOf("foo.bar")
                this.outputFile = File("./ts.d.ts")
            }
        }


        // then
        verify(mockExtension).configure(eq("kt2ts"), any<Action<Kt2TsPluginExtension>>())
    }

    @Test
    fun `Kt2TsPluginExtension is mutable`() {
        val extension = Kt2TsPluginExtension()
        val settings: List<() -> Any?> =
            listOf(
                { extension.generationSpecifications.firstOrNull()?.outputFile },
                { extension.generationSpecifications.firstOrNull()?.annotations },
                { extension.classFilesSources.compileTasks },
                { extension.classFilesSources.classesDirs }
            )

        assertThat(settings.mapNotNull { it.invoke() }, hasSize(0))

        extension.generationSpecification {
            outputFile = File("./")
            annotations = listOf("foo")
        }
        extension.classFilesSources {
            classesDirs = mock()
            compileTasks = mock()
        }

        assertThat(settings.mapNotNull { it.invoke() }, hasSize(4))
    }
}
