package se.jensim.gradle.plugin.kt2ts.internal

import kotlin.reflect.KClass

interface ClassFinder {

    fun getAnnotatedClasses(annotationClassRef: String): Set<KClass<*>>
}
