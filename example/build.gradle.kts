import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import se.jensim.gradle.plugin.kt2ts.kt2ts

plugins {
    kotlin("jvm") version "1.3.31"
    id("se.jensim.kt2ts") version "0.3.0"
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
    //Optional:
    //classesDirs = files("$buildDir/classes/kotlin/main", "$buildDir/classes/java/main")
    //outputFile = file("$buildDir/ts/kt2ts.d.ts")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

group = "se.jensim.kt2ts-example"
version = "0.4.0-SNAPSHOT"
