package se.jensim.gradle.plugin.kt2ts

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections

open class Kt2TsTask : DefaultTask() {

    init {
        description = "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
    }

    @InputFiles
    fun getSourceFiles(): FileCollection =
        project.extensions.getByType(Kt2TsPluginExtension::class.java).classesDirs
            ?: throw Kt2TsException("No classesDirs defined in kt2ts config extension.")

    @OutputFile
    fun getOutput() = project.extensions.getByType(Kt2TsPluginExtension::class.java).outputFile
        ?: throw Kt2TsException("No outputFile defined in kt2ts config extension.")

    @TaskAction
    open fun generateTypescript() {
        val annotatedClasses = getAnnotatedClasses()
        val ts = generateTypescript(annotatedClasses)
        write(ts)
    }

    private fun getAnnotatedClasses(): Set<Class<*>> {
        if (getSourceFiles().isEmpty) {
            throw Kt2TsException("Unable to find source files")
        }
        val extension = project.extensions.getByType(Kt2TsPluginExtension::class.java)
        val classesURLs = getSourceFiles()
            .map { it.toURI().toURL() }
            .toTypedArray()

        val classLoader = java.net.URLClassLoader(classesURLs)
        val annotation = try {
            @Suppress("UNCHECKED_CAST")
            classLoader.loadClass(extension.annotation) as Class<Annotation>
        } catch (e: Exception) {
            throw Kt2TsException("Bad choise of annotation", e)
        }

        val reflections = Reflections(classLoader)
        return reflections.getTypesAnnotatedWith(annotation, true)
    }

    private fun generateTypescript(classes: Set<Class<*>>): String = TypeScriptGenerator(
        rootClasses = classes.map { it.kotlin }
    ).individualDefinitions.joinToString("\n\n") { "export $it" }

    private fun write(data: String) {
        val output = getOutput()
        val dir = output.parentFile
        dir.mkdirs()
        output.writeText(data)
    }
}
