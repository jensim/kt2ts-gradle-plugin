package se.jensim.gradle.plugin.kt2ts.internal

import se.jensim.gradle.plugin.kt2ts.Kt2TsPluginExtension

internal interface Kt2TsService {
    fun generateTypescript(extension: Kt2TsPluginExtension)
}
