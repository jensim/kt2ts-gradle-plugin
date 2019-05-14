package se.jensim.gradle.plugin.kt2ts

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import java.io.File

open class Kt2TsTask : DefaultTask() {

    private val fullPath get() = project.extensions.getByType(Kt2TsPluginExtension::class.java).outputFile!!.absolutePath!!
    private val classesURLs get() = getSourceFiles().map { it.toURI().toURL() }.toTypedArray()

    init {
        description =
            "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
        dependsOn(
            listOfNotNull(
                project.tasks.findByName("compileKotlin"),
                project.tasks.findByName("compileJava")
            )
        )
    }

    @InputFiles
    fun getSourceFiles(): FileCollection =
        project.extensions.getByType(Kt2TsPluginExtension::class.java).classesDirs!!

    @OutputFile
    fun getOutput() = File(fullPath)

    @TaskAction
    open fun generateTypescript() {
        val extension = project.extensions.getByType(Kt2TsPluginExtension::class.java)

        val classLoader = java.net.URLClassLoader(classesURLs)
        val annotation = try {
            classLoader.loadClass(extension.annotation) as Class<Annotation>
        } catch (e: Exception) {
            throw RuntimeException("Bad choise of annotation", e)
        }

        val reflections = Reflections(classLoader)
        val annotated = reflections.getTypesAnnotatedWith(annotation, false)

        val tsParts = TypeScriptGenerator(
            rootClasses = annotated.mapNotNull { it.kotlin }
        ).individualDefinitions
        val ts = tsParts.joinToString("\n\n") { "export $it" }

        val output = getOutput()
        val dir = output.parentFile
        dir.mkdirs()
        output.writeText(ts)
    }
}
