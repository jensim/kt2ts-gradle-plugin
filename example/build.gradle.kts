import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:1.0.0-SNAPSHOT")
    }
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        mavenLocal()
    }
}
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
    implementation("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:1.0.0-SNAPSHOT")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
