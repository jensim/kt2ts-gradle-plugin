import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.jensim.gradle.plugin.kt2ts.kt2ts

buildscript {
    repositories {
        repositories {
            mavenLocal()
            gradlePluginPortal()
        }
    }
    dependencies {
        classpath("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:DEV")
    }
}
apply(plugin = "se.jensim.kt2ts")
plugins {
    kotlin("jvm") version "1.3.31"
}
repositories {
    mavenCentral()
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test-junit"))
}

kt2ts {
    generationSpecification {
        outputFile = file("$buildDir/ts/kt2ts.d.ts")
        annotations = listOf("com.example.ToTypescript")
    }
    addGenerationSpecification {
        annotations = listOf("com.example.MyJavaAnnotation")
        outputFile = file("$buildDir/ts/java-only.d.ts")
    }
    classFilesSources {
        compileTasks = listOf(tasks.compileKotlin, tasks.compileJava)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
