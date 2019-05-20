package se.jensim.gradle.plugin.kt2ts.internal

import java.io.File
import kotlin.reflect.KClass

interface Kt2TsTypeScriptWriter {

    fun write(classes: Map<File, Collection<KClass<*>>>)
}
