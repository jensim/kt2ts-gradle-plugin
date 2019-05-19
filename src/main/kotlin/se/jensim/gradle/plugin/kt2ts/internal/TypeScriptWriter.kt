package se.jensim.gradle.plugin.kt2ts.internal

import kotlin.reflect.KClass

interface TypeScriptWriter {

    fun write(classes: Set<KClass<*>>)
}
