package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import se.jensim.gradle.plugin.kt2ts.internal.*
import java.io.File

open class Kt2TsTask : DefaultTask() {

    init {
        description = "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
    }

    internal var classfinderFactory: (FileCollection) -> ClassFinder = { DefaultClassFinder(it.files) }
    internal var typeScriptWriterFactory: (File) -> TypeScriptWriter = { DefaultTypeScriptWriter(it) }

    private val extension by lazy { project.extensions.getByType(Kt2TsPluginExtension::class.java) }

    @InputFiles
    fun getSourceFiles(): FileCollection = extension.classesDirs ?: throw Kt2TsException(
        "No classesDirs defined in kt2ts config extension."
    )

    @OutputFile
    fun getOutput(): File = extension.outputFile
        ?: throw Kt2TsException("No outputFile defined in kt2ts config extension.")

    @TaskAction
    open fun generateTypescript() {
        val annotatedClasses = classfinderFactory.invoke(getSourceFiles())
            .getAnnotatedClasses(extension.annotation!!)
        typeScriptWriterFactory.invoke(getOutput()).write(annotatedClasses)
    }
}
