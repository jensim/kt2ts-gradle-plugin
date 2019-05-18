package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import se.jensim.gradle.plugin.kt2ts.internal.ClassFinder
import se.jensim.gradle.plugin.kt2ts.internal.Kt2TsException
import se.jensim.gradle.plugin.kt2ts.internal.TypeScriptWriter

open class Kt2TsTask : DefaultTask() {

    init {
        description = "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
    }

    private val extension by lazy { project.extensions.getByType(Kt2TsPluginExtension::class.java) }

    @InputFiles
    fun getSourceFiles(): FileCollection = extension.classesDirs ?: throw Kt2TsException(
        "No classesDirs defined in kt2ts config extension."
    )

    @OutputFile
    fun getOutput() = extension.outputFile ?: throw Kt2TsException("No outputFile defined in kt2ts config extension.")

    @TaskAction
    open fun generateTypescript() {
        val annotatedClasses = ClassFinder(getSourceFiles().files)
            .getAnnotatedClasses(extension.annotation!!)
        TypeScriptWriter(getOutput()).write(annotatedClasses)
    }
}
