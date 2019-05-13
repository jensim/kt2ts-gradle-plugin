import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.jensim.gradle.plugin.kt2ts.kt2ts

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath(kotlin(module = "gradle-plugin", version = "1.3.31"))
        classpath("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:0.1.0-SNAPSHOT")
    }
}
repositories {
    mavenCentral()
}
apply(plugin = "se.jensim.kt2ts")
plugins {
    kotlin("jvm") version "1.3.31"
}

kt2ts {
    annotation = "com.example.ToTypescript"
}
kt2ts.annotation = "com.example.ToTypescript"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
