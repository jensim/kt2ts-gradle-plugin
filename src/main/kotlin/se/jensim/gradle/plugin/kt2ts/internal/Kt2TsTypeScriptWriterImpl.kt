package se.jensim.gradle.plugin.kt2ts.internal

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import java.io.File
import kotlin.reflect.KClass

internal class Kt2TsTypeScriptWriterImpl : Kt2TsTypeScriptWriter {

    override fun write(classes: Map<File, Collection<KClass<*>>>) {
        classes.forEach { (t, u) -> write(t, u) }
    }

    private fun write(destination: File, classes: Collection<KClass<*>>) {
        val ts = TypeScriptGenerator(
            rootClasses = classes
        ).individualDefinitions.joinToString("\n\n") { "export $it" }

        val dir = destination.parentFile
        dir.mkdirs()
        destination.writeText(ts)
    }
}
