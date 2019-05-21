package se.jensim.gradle.plugin.kt2ts.internal

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import se.jensim.gradle.plugin.kt2ts.Kt2TsPluginExtension
import se.jensim.gradle.plugin.kt2ts.internal.classfilefindertest.ClassToFind

class Kt2TsServiceImplTest {

    private val extension = Kt2TsPluginExtension().apply {
        output {
            outputFile = mock()
            annotations = listOf("foo.bar")
        }
        classFilesSources {
            classesDirs = mock {
                on { files } doReturn setOf(mock())
            }
        }
    }
    private val annotationMapping = mapOf("foo.bar" to setOf(ClassToFind::class))
    private val finderMock: Kt2TsClassFinder = mock {
        on { mapAnnotationsToAnnotatedClasses(any()) } doReturn annotationMapping
    }
    private val writerMock: Kt2TsTypeScriptWriter = mock()
    private val service = Kt2TsServiceImpl(finderMock, writerMock)

    @Test
    fun generateTypescript() {
        // when
        service.generateTypescript(extension)

        // then
        verify(writerMock).write(any())
    }
}
