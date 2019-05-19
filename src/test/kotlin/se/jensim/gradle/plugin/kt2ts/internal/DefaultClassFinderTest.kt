package se.jensim.gradle.plugin.kt2ts.internal

import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import se.jensim.gradle.plugin.kt2ts.internal.classfilefindertest.AnnotationToFind
import se.jensim.gradle.plugin.kt2ts.internal.classfilefindertest.SecondClassToFind
import java.io.File
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class DefaultClassFinderTest {

    private val classPath = "build/classes/kotlin/test/se/jensim/gradle/plugin/kt2ts/internal/classfilefindertest"

    @Test
    fun `find annotated test class`() {
        // given
        val sourceFiles =
            setOf(File(classPath))
        val finder = DefaultClassFinder(sourceFiles)
        val annotation = AnnotationToFind::class

        // when
        val c = finder.getAnnotatedClasses(annotation.qualifiedName!!)

        // then
        assertThat(c, hasSize(1))
        assertSame(c.first(), SecondClassToFind::class)
    }

    @Test
    fun `bad annotation`() {
        // given
        val sourceFiles = setOf(File(classPath))
        val finder = DefaultClassFinder(sourceFiles)

        // when
        assertFailsWith<Kt2TsException> {
            finder.getAnnotatedClasses("foo.bar")
        }
    }

    @Test
    fun `bad source`() {
        // given
        val sourceFiles = emptySet<File>()
        val finder = DefaultClassFinder(sourceFiles)
        val annotation = AnnotationToFind::class

        // when
        assertFailsWith<Kt2TsException> {
            finder.getAnnotatedClasses(annotation.qualifiedName!!)
        }
    }
}
