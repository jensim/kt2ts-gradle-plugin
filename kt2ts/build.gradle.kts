import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
    `java-gradle-plugin`

}
repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

group = "se.jensim.kt2ts"
version = "1.0-SNAPSHOT"


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    implementation("com.github.ntrrgc:ts-generator:1.1.1")
    implementation("org.reflections:reflections:0.9.11")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

gradlePlugin {
    plugins {
        create("kt2ts") {
            id = "se.jensim.kt2ts"
            version = "1.0.0-SNAPSHOT"
            implementationClass = "se.jensim.gradle.plugin.kt2ts.Kt2TsPlugin"
            dependencies {
                implementation("com.github.ntrrgc:ts-generator:1.1.1")
                implementation("org.reflections:reflections:0.9.11")
            }
            repositories {
                mavenCentral()
                mavenLocal()
                maven("https://jitpack.io")
            }
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
