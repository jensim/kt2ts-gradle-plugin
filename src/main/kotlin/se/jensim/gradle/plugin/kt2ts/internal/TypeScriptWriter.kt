package se.jensim.gradle.plugin.kt2ts.internal

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import java.io.File
import kotlin.reflect.KClass

internal class TypeScriptWriter(private val destination: File) {

    fun write(classes: Set<KClass<*>>) {
        val ts = TypeScriptGenerator(
            rootClasses = classes
        ).individualDefinitions.joinToString("\n\n") { "export $it" }

        val dir = destination.parentFile
        dir.mkdirs()
        destination.writeText(ts)
    }
}
