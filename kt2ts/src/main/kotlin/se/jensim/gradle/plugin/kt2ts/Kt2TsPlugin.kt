package se.jensim.gradle.plugin.kt2ts

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import java.io.File


open class Kt2TsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)
        val task = target.tasks.create("kt2ts", Kt2TsTask::class.java)
        task.dependsOn(
            listOfNotNull(
                target.tasks.findByName("compileKotlin"),
                target.tasks.findByName("compileJava")
            )
        )
    }
}

open class Kt2TsTask : DefaultTask() {

    private val myInputs: List<File> = listOf("kotlin", "java").mapNotNull {
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

    private val sourceFiles: FileCollection = project.files(myInputs)
    private val buildDir = "${project.buildDir}/ts"
    private val fileName = "kt2ts.d.ts"
    private val fullPath = "$buildDir/$fileName"

    private val classesURLs
        get() =
            sourceFiles.map { it.toURI().toURL() }.toTypedArray()


    init {
        description =
            "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
    }

    @InputFiles
    fun getSourceFiles() = this.sourceFiles

    @OutputFile
    fun getOutputDir() = File(fullPath)


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

        val dir = File("${project.buildDir}", "ts")
        dir.mkdirs()
        val output = File(dir, "kt2ts.d.ts")
        output.writeText(ts)
    }
}
