package se.jensim.gradle.plugin.kt2ts.internal

import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import se.jensim.gradle.plugin.kt2ts.internal.classfilefindertest.AnnotationToFind
import se.jensim.gradle.plugin.kt2ts.internal.classfilefindertest.SecondClassToFind
import java.io.File
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Kt2TsClassFinderImplTest {

    private val classPath = "build/classes/kotlin/test/se/jensim/gradle/plugin/kt2ts/internal/classfilefindertest"

    @Test
    fun `find annotated test class`() {
        // given
        val sourceFiles =
            setOf(File(classPath))
        val finder = Kt2TsClassFinderImpl(sourceFiles)
        val annotation = AnnotationToFind::class

        // when
        val c = finder.mapAnnotationsToAnnotatedClasses(
            setOf(annotation.qualifiedName!!))

        // then
        assertThat (c.keys, hasSize(1))
        assertThat(c.values.first(), hasSize(1))
        val expected = c.values.first().first()
        assertSame(expected, SecondClassToFind::class)
    }

    @Test
    fun `bad annotation`() {
        // given
        val sourceFiles = setOf(File(classPath))
        val finder = Kt2TsClassFinderImpl(sourceFiles)

        // when
        assertFailsWith<Kt2TsException> {
            finder.mapAnnotationsToAnnotatedClasses(setOf("foo.bar"))
        }
    }

    @Test
    fun `bad source`() {
        // given
        val sourceFiles = emptySet<File>()
        val finder = Kt2TsClassFinderImpl(sourceFiles)
        val annotation = AnnotationToFind::class

        // when
        assertFailsWith<Kt2TsException> {
            finder.mapAnnotationsToAnnotatedClasses(setOf(annotation.qualifiedName!!))
        }
    }
}
