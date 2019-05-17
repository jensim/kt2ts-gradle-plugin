import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    id("se.jensim.kt2ts") version "0.7.8-rc3"
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
    classesDirs = files(
        tasks.findByName("compileKotlin")?.outputs,
        tasks.findByName("compileJava")?.outputs
    )
    outputFile = file("$buildDir/ts/kt2ts.d.ts")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

group = "se.jensim.kt2ts-example"
version = "DEV"
