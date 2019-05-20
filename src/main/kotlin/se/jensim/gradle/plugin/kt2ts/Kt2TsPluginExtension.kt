package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import java.io.File

open class Kt2TsPluginExtension {

    /**
     * <pre>
     * {@code
     * kt2ts {
     *   generationSpecifications = arrayListOf(GenerationSpecification().apply{
     *     outputFile = file("$buildDir/ts/kt2ts.d.ts")
     *     annotations = listOf("com.example.MyAnnotation")
     *   })
     *   }
     * }
     * }
     * </pre>
     */
    var generationSpecifications: MutableList<GenerationSpecification> = ArrayList()

    /**
     * Configure where to look for class files wearing the annotation you specified.
     * All combinations are applied as a total when used in the plugin.
     * Property access verion
     */
    val classFilesSources = ClassFileExtension()

    /**
     * Configure where to look for class files wearing the annotation you specified.
     * All combinations are applied as a total when used in the plugin.
     * Kotlin-DSL version.
     */
    fun classFilesSources(config: ClassFileExtension.() -> Unit) {
        classFilesSources.apply(config)
    }

    val classDirFiles: List<File>
        get() = listOfNotNull(
            classFilesSources.classesDirs?.files?.toList(),
            classFilesSources.compileTasks?.map { it.get().outputs.files.files }?.flatten()
        ).flatten().filterNotNull()

    /**
     * <pre>
     * {@code
     * kt2ts {
     *   addGenerationSpecification {
     *     outputFile = file("$buildDir/ts/kt2ts.d.ts")
     *     annotations = listOf("com.example.MyAnnotation")
     *   }
     * }
     * }
     * </pre>
     */
    fun generationSpecification(config: GenerationSpecification.() -> Unit) {
        val element = GenerationSpecification().apply(config)
        require(element.outputFile != null) { "addGenerationSpecification.outputFile is required" }
        require(element.annotations?.isNotEmpty() == true) { "addGenerationSpecification.annotations is required" }
        generationSpecifications.add(element)
    }

    /**
     * Configure where to look for class files wearing the annotation you specified
     */
    class ClassFileExtension {

        /**
         *
         * <code>classFilesSources {
         *      classesDirs = files("$buildDir/classes/kotlin/main")
         * }</code>
         */
        var classesDirs: FileCollection? = null

        /**
         *
         * <code>classFilesSources {
         *      compileTasks = listOf(tasks.compileKotlin, tasks.compileJava)
         * }</code>
         */
        var compileTasks: List<TaskProvider<*>>? = null

        fun reset() {
            classesDirs = null
            compileTasks = null
        }
    }

    class GenerationSpecification {

        /**
         * What annotations to look for when discovering classes to generate Typescript for
         */
        var annotations: List<String>? = null

        /**
         * where to output the resulting
         */
        var outputFile: File? = null
    }
}

fun Project.kt2ts(config: Kt2TsPluginExtension.() -> Unit) {
    @Suppress("UnstableApiUsage")
    extensions.configure("kt2ts", config)
}

val TaskContainer.kt2ts: TaskProvider<Kt2TsTask> get() = named("kt2ts", Kt2TsTask::class.java)
