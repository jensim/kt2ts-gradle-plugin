package se.jensim.gradle.plugin.kt2ts.internal

import se.jensim.gradle.plugin.kt2ts.Kt2TsPluginExtension

internal open class Kt2TsServiceImpl(
    private val classFinder: Kt2TsClassFinder,
    private val writer: Kt2TsTypeScriptWriter
) : Kt2TsService {
    override fun generateTypescript(extension: Kt2TsPluginExtension) {
        val annotationToTarget = extension.outputs
            .flatMap { it.annotations!! }
            .let { classFinder.mapAnnotationsToAnnotatedClasses(it) }
        val toGenerate = extension.outputs.map {
            it.outputFile!! to it.annotations!!.flatMap { annotationToTarget[it]!!.toList() }
        }.toMap()
        writer.write(toGenerate)
    }
}
