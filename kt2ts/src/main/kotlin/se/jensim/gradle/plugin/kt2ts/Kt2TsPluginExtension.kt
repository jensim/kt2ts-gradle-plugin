package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.Project

open class Kt2TsPluginExtension {
    var annotation: String = "se.jensim.gradle.plugin.kt2ts.Kotlin2Typescript"
}

val Project.`kt2ts`: Kt2TsPluginExtension
    get() = extensions.getByName("kt2ts") as Kt2TsPluginExtension

fun Project.`kt2ts`(config: Kt2TsPluginExtension.() -> Unit): Unit =
    extensions.configure("kt2ts", config)
