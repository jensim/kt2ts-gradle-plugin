package se.jensim.gradle.plugin.kt2ts.internal

import org.reflections.Reflections
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.KClass

internal class Kt2TsClassFinderImpl(private val sourceFiles: Collection<File>) : Kt2TsClassFinder {

    private val classLoader: ClassLoader by lazy {
        if (sourceFiles.isEmpty()) {
            throw Kt2TsException("Unable to find source files")
        }
        val classesURLs = sourceFiles
            .map { it.toURI().toURL() }
            .toTypedArray()

        URLClassLoader(classesURLs)
    }

    override fun mapAnnotationsToAnnotatedClasses(annotationClassRefs: Collection<String>): Map<String, Set<KClass<*>>> =
        annotationClassRefs.toSet()
            .map { it to getAnnotatedClasses(it) }
            .toMap()

    private fun getAnnotatedClasses(annotationClassRef: String): Set<KClass<*>> {
        val annotation = try {
            @Suppress("UNCHECKED_CAST")
            classLoader.loadClass(annotationClassRef) as Class<Annotation>
        } catch (e: Exception) {
            throw Kt2TsException("Bad choice of annotation", e)
        }

        val reflections = Reflections(classLoader)
        return reflections.getTypesAnnotatedWith(annotation, true)
            .map { it.kotlin }.toSet()
    }
}
