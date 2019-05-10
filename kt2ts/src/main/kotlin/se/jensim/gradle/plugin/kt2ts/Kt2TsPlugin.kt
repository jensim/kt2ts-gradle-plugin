package se.jensim.gradle.plugin.kt2ts

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import java.io.File

open class Kt2TsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.buildscript.repositories.maven {
            it.setUrl("https://jitpack.io")
        }
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

    val buildDir = "${project.buildDir}/ts"
    val fileName = "kt2ts.d.ts"
    val fullPath = "$buildDir/$fileName"

    init {
        description =
            "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
        inputs.dir(buildDir)
        outputs.file(fullPath)
    }

    @TaskAction
    open fun generateTypescript() {
        val kotlinClassPath = File("${project.buildDir.absolutePath}/classes/kotlin/main").toURI().toURL()
        val listOfURL = arrayListOf(kotlinClassPath).toTypedArray()

        val classLoader = java.net.URLClassLoader(listOfURL)

        val reflections = Reflections(classLoader)
        val annotated = reflections.getTypesAnnotatedWith(Kotlin2Typescript::class.java, false)

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
