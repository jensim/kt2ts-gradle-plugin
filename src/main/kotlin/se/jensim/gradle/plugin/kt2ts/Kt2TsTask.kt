package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import se.jensim.gradle.plugin.kt2ts.internal.*
import java.io.File

open class Kt2TsTask : DefaultTask() {

    init {
        description =
            "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
    }

    private val extension by lazy { project.extensions.getByType(Kt2TsPluginExtension::class.java) }
    private val classFinder by lazy { Kt2TsClassFinderImpl(getSourceFiles()) }
    internal var overrideService: Kt2TsService? = null
    private val service: Kt2TsService by lazy { overrideService ?: Kt2TsServiceImpl(classFinder, Kt2TsTypeScriptWriterImpl()) }

    @InputFiles
    fun getSourceFiles(): List<File> = extension.classDirFiles.also {
        if (it.isEmpty()) {
            throw Kt2TsException("No classesDirs defined in kt2ts config extension.")
        }
    }

    @OutputFiles
    fun getOutput(): List<File> = extension.outputs.map { it.outputFile!! }.also {
        if (it.isEmpty()) {
            throw Kt2TsException("No outputFile defined in kt2ts config extension.")
        }
    }

    @TaskAction
    open fun generateTypescript() {
        service.generateTypescript(extension)
    }
}
