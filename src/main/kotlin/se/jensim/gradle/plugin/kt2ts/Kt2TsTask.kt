package se.jensim.gradle.plugin.kt2ts

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import java.io.File

open class Kt2TsTask : DefaultTask() {

    private val defaultInputs: List<File> = listOf("kotlin", "java").mapNotNull {
        try {
            val file = File("${project.buildDir.absolutePath}/classes/$it/main")
            if (file.exists() && file.isDirectory && file.canRead()) {
                file
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private val buildDir = "${project.buildDir}/ts"
    private val fileName = "kt2ts.d.ts"
    private val fullPath
        get() = try {
            project.extensions.getByType(Kt2TsPluginExtension::class.java).outputFile!!.absolutePath!!
        } catch (e: Exception) {
            when (e) {
                is UnknownDomainObjectException,
                is NullPointerException -> "$buildDir/$fileName"
                else -> throw e
            }
        }

    private val classesURLs
        get() =
            getSourceFiles().map { it.toURI().toURL() }.toTypedArray()

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
    fun getSourceFiles(): FileCollection = try {
        project.extensions.getByType(Kt2TsPluginExtension::class.java).classesDirs!!
    } catch (e: Exception) {
        when (e) {
            is UnknownDomainObjectException,
            is NullPointerException -> project.files(defaultInputs)
            else -> throw e
        }
    }

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
