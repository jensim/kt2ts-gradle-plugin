package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.Plugin
import org.gradle.api.Project

open class Kt2TsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create("kt2ts", Kt2TsPluginExtension::class.java)
        val task = target.tasks.create("kt2ts", Kt2TsTask::class.java)

        target.tasks.findByName("processResources")
            ?.dependsOn(task)
            ?: System.err.println("Could not find the processResources task. You might need to set up task order manually.")

        val dependencies = listOfNotNull(
            target.tasks.find { it.name.startsWith("compile") }
        ).filterNot { it.name.startsWith("compileTest") }

        if (dependencies.isEmpty()) {
            System.err.println("Could not find any compile tasks. You might need to set up task order manually.")
        } else {
            task.dependsOn(dependencies)
        }
    }
}
