package se.jensim.gradle.plugin.kt2ts

import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import se.jensim.gradle.plugin.kt2ts.internal.Kt2TsException
import java.io.File

open class Kt2TsPluginExtension {

    private var project: Project? = null

    fun setProject(target: Project) {
        project = target
    }

    var annotation: String?
        get() = outputs.firstOrNull()?.annotations?.firstOrNull()
        set(value) {
            if (value != null) {
                output {
                    annotations = listOf(value)
                }
            }
        }

    var annotations: List<String>?
        get() = outputs.mapNotNull { it.annotations }.flatten()
        set(value) {
            if (value != null) {
                output {
                    annotations = value
                }
            }
        }

    /**
     * <pre>
     * {@code
     * kt2ts {
     *   outputs = arrayListOf(GenerationSpecification().apply{
     *     outputFile = file("$buildDir/ts/kt2ts.d.ts")
     *     annotations = listOf("com.example.MyAnnotation")
     *   })
     *   }
     * }
     * }
     * </pre>
     */
    var outputs: MutableList<GenerationSpecification> = ArrayList()

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
        get() {
            val summed = listOfNotNull(
                classFilesSources.classesDirs?.files?.toList(),
                classFilesSources.compileTasks?.map { it.get().outputs.files.files }?.flatten()
            ).flatten().filterNotNull()
            return if (summed.isEmpty()) {
                if (project != null) {
                    val backup = listOfNotNull(
                        project?.safeGetTaskOutputs("compileJava"),
                        project?.safeGetTaskOutputs("compileKotlin")
                    ).flatten()
                    if (backup.isEmpty()) {
                        throw Kt2TsException("No classdirs defined and unable to use defaults.")
                    } else {
                        backup
                    }
                } else {
                    throw Kt2TsException("No classdirs defined and unable to use defaults.")
                }
            } else {
                summed
            }
        }

    private fun Project.safeGetTaskOutputs(name: String): List<File>? = try {
        project.tasks.findByName(name)?.outputs?.files?.files?.toList()
    } catch (e: Exception) {
        emptyList()
    }

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
    fun output(config: GenerationSpecification.() -> Unit) {
        val element = GenerationSpecification().apply(config)
        if (element.outputFile == null && outputs.isEmpty()) {
            project?.let { element.outputFile = it.file("${it.buildDir}/ts/kt2ts.d.ts") }
        }
        require(element.outputFile != null) { "addGenerationSpecification.outputFile is required" }
        require(element.annotations?.isNotEmpty() == true) { "addGenerationSpecification.annotations is required" }
        require(!outputs.mapNotNull { it.outputFile }.contains(element.outputFile!!)) { "Output file ${element.outputFile} already registered." }
        outputs.add(element)
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

val Project.kt2ts: Kt2TsPluginExtension
    get() {
        val e = extensions.getByType(Kt2TsPluginExtension::class.java)
        e.setProject(this)
        return e
    }

fun Project.kt2ts(config: Kt2TsPluginExtension.() -> Unit) {
    @Suppress("UnstableApiUsage")
    extensions.configure("kt2ts", config)
    extensions.getByType(Kt2TsPluginExtension::class.java).setProject(this)
}

val TaskContainer.kt2ts: TaskProvider<Kt2TsTask> get() = named("kt2ts", Kt2TsTask::class.java)
