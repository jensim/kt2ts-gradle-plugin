package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.Plugin
import org.gradle.api.Project


open class Kt2TsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)
        val task = target.tasks.create("kt2ts", Kt2TsTask::class.java)
        target.tasks.findByName("processResources")?.dependsOn(task)
        task.dependsOn(
            listOfNotNull(
                target.tasks.findByName("compileKotlin"),
                target.tasks.findByName("compileJava")
            )
        )
    }
}
