import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        jcenter()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.0.0")
    }
}
plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`
}
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

group = "se.jensim.kt2ts"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    compileOnly("com.github.ntrrgc:ts-generator:1.1.1")
    implementation("org.reflections:reflections:0.9.11")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<Jar> {
    val deps = listOf("ts-generator")
        .joinToString("|", "^(", ")$") { "$it.*.jar" }
    configurations["compileClasspath"]
        .filter { it.name.matches(Regex(deps)) }
        .forEach { file: File ->
            from(zipTree(file.absoluteFile))
        }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

gradlePlugin {
    plugins {
        create("kt2ts") {
            id = "se.jensim.kt2ts"
            version = "0.1.0-SNAPSHOT"
            implementationClass = "se.jensim.gradle.plugin.kt2ts.Kt2TsPlugin"
        }
    }
}

publishing {
    repositories {
        /*maven {
            url = uri("../../consuming/maven-repo")
        }
        ivy {
            url = uri("../../consuming/ivy-repo")
        }*/
        mavenLocal()
    }
}
