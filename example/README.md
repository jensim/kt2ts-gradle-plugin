````kts

buildscript {
    dependencies {
        classpath("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:1.0.0-SNAPSHOT")
    }
    repositories {
        maven("https://jitpack.io")
    }
}

apply(plugin = "se.jensim.kt2ts")

dependencies {
    implementation("se.jensim.kt2ts:se.jensim.kt2ts.gradle.plugin:1.0.0-SNAPSHOT")
}
````
