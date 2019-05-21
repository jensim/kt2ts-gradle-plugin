import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    id("se.jensim.kt2ts") version "0.9.0"
}

repositories {
    mavenCentral()
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test-junit"))
}

kt2ts {
    output {
        outputFile = file("$buildDir/ts/kt2ts.d.ts")
        annotations = listOf("com.example.ToTypescript")
    }
    output {
        annotations = listOf("com.example.MyJavaAnnotation")
        outputFile = file("$buildDir/ts/java-only.d.ts")
    }
    classFilesSources {
        compileTasks = listOf(tasks.compileKotlin, tasks.compileJava)
        classesDirs = files("$buildDir/classes/kotlin/main")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

group = "se.jensim.kt2ts-example"
version = "DEV"
