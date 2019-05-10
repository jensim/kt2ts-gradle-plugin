import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        // THIS:
        classpath("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:1.0.0-SNAPSHOT")
    }
    repositories {
        mavenCentral()
        // THIS: for ts-generator dependency
        maven("https://jitpack.io")
        mavenLocal()//Never mind this - its for dev purposes
    }
}
// THIS: for the task
apply(plugin = "se.jensim.kt2ts")
plugins {
    kotlin("jvm") version "1.3.31"
}
repositories {
    mavenCentral()
    mavenLocal()
}

group = "com.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // THIS: for the annotation
    implementation("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:1.0.0-SNAPSHOT")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
