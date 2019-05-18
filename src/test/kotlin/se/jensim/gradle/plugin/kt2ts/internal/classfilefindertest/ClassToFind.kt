package se.jensim.gradle.plugin.kt2ts.internal.classfilefindertest

internal data class ClassToFind(val foo: String)

internal annotation class AnnotationToFind

@AnnotationToFind
internal data class SecondClassToFind(val bar: String)
