import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.jensim.gradle.plugin.kt2ts.kt2ts

buildscript {
    repositories {
        repositories {
            mavenCentral()
            mavenLocal()
            gradlePluginPortal()
        }
    }
    dependencies {
        classpath("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:0.4.0-SNAPSHOT")
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
    annotation = "com.example.ToTypescript"
    classesDirs = files("$buildDir/classes/kotlin/main")
    outputFile = file("$buildDir/ts.d.ts")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
