import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.jensim.gradle.plugin.kt2ts.kt2ts

buildscript {
    dependencies {
        // THIS:
        classpath("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:0.1.0-SNAPSHOT")
    }
    repositories {
        mavenCentral()
        mavenLocal()
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

kt2ts {
    annotation = "com.example.ToTypescript"
}

group = "com.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
