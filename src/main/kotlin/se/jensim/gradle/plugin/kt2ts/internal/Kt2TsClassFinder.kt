package se.jensim.gradle.plugin.kt2ts.internal

import kotlin.reflect.KClass

interface Kt2TsClassFinder {

    fun mapAnnotationsToAnnotatedClasses(annotationClassRefs: Collection<String>): Map<String, Set<KClass<*>>>
}
