package se.jensim.gradle.plugin.kt2ts.internal

import org.reflections.Reflections
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.KClass

internal class ClassFinder(private val sourceFiles: Set<File>) {

    private val classLoader: ClassLoader by lazy {
        if (sourceFiles.isEmpty()) {
            throw Kt2TsException("Unable to find source files")
        }
        val classesURLs = sourceFiles
            .map { it.toURI().toURL() }
            .toTypedArray()

        URLClassLoader(classesURLs)
    }

    fun getAnnotatedClasses(annotationClassRef: String): Set<KClass<*>> {
        val annotation = try {
            @Suppress("UNCHECKED_CAST")
            classLoader.loadClass(annotationClassRef) as Class<Annotation>
        } catch (e: Exception) {
            throw Kt2TsException("Bad choise of annotation", e)
        }

        val reflections = Reflections(classLoader)
        return reflections.getTypesAnnotatedWith(annotation, true)
            .map { it.kotlin }.toSet()
    }
}
