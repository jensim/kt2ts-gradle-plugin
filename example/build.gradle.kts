import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.jensim.gradle.plugin.kt2ts.kt2ts

plugins {
    kotlin("jvm") version "1.3.31"
    id("se.jensim.kt2ts") version "0.1.0-SNAPSHOT"
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
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

group = "se.jensim.kt2ts-example"
version = "0.1.0-SNAPSHOT"
