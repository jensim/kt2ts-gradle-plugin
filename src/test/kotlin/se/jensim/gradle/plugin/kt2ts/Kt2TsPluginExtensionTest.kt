package se.jensim.gradle.plugin.kt2ts

import com.nhaarman.mockitokotlin2.*
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasSize
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.File

class Kt2TsPluginExtensionTest {

    @Test
    fun `kt2ts extension func`() {
        // given
        val setter: Kt2TsPluginExtension.() -> Unit = {
            annotation = "foo.bar"
            outputFile = File("/")
        }
        val mockExtension: ExtensionContainer = mock()
        val mockProject: Project = mock {
            on { extensions } doReturn mockExtension
        }

        // when
        mockProject.kt2ts(setter)

        // then
        verify(mockExtension).configure(eq("kt2ts"), any<Action<Kt2TsPluginExtension>>())
    }

    @Test
    fun `Kt2TsPluginExtension is mutable`() {
        val extension = Kt2TsPluginExtension()
        val settings = listOf({ extension.outputFile }, { extension.classesDirs }, { extension.annotation })

        assertThat(settings.mapNotNull { it.invoke() }, hasSize(0))

        extension.outputFile = File("/")
        extension.classesDirs = mock()
        extension.annotation = "foo"

        assertThat(settings.mapNotNull { it.invoke() }, hasSize(3))
    }
}
