package se.jensim.gradle.plugin.kt2ts

import me.ntrrgc.tsGenerator.TypeScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.reflections.Reflections
import java.io.File
import java.net.URI
import java.net.URL
import java.util.*

open class Kt2TsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val task = target.tasks.create("kt2ts", Kt2TsTask::class.java)
        task.dependsOn(
            listOfNotNull(
                target.tasks.findByName("compileKotlin"),
                target.tasks.findByName("compileJava")
            )
        )
        target.repositories.maven {
            it.url = URI("https://jitpack.io")
        }
    }
}


open class Kt2TsTask : DefaultTask() {

    val buildDir = "${project.buildDir}/ts"
    val fileName = "kt2ts.d.ts"
    val fullPath = "$buildDir/$fileName"

    init {
        description =
            "Generate typescript definitions from Kotlin files annotated with the magic word Kotlin2Typescript"
        inputs.dir("${project.buildDir}")
        outputs.file(fullPath)
    }

    @TaskAction
    open fun generateTypescript() {
        val listOfURL = ArrayList<URL>()

        val kotlinClassPath = File("${project.buildDir.absolutePath}/classes/kotlin/main").toURI().toURL()
        listOfURL.add(kotlinClassPath)

        val classLoader = java.net.URLClassLoader(listOfURL.toTypedArray())

        val reflections = Reflections("com.example", classLoader)
        val annotated = reflections.getTypesAnnotatedWith(Kotlin2Typescript::class.java, false)

        val tsParts = TypeScriptGenerator(
            rootClasses = annotated.mapNotNull { it.kotlin }
        ).individualDefinitions
        val ts = tsParts.joinToString("\n\n") { "export $it" }

        println("Generated ${tsParts.size} typescript definitions")

        val dir = File("${project.buildDir}", "ts")
        dir.mkdirs()
        val output = File(dir, "kt2ts.d.ts")
        output.writeText(ts)

    }
}
