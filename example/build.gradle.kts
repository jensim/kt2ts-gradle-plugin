import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.jensim.gradle.plugin.kt2ts.kt2ts

plugins {
    id("se.jensim.kt2ts") version "0.1.0-SNAPSHOT"
    kotlin("jvm") version "1.3.31"
}

kt2ts {
    annotation = "com.example.ToTypescript"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
