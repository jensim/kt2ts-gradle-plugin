# Example use of kt2ts plugin

## How to do it?

Have a look at the [build-file](build.gradle.kts)
You need:
- an [annotation](src/main/kotlin/com/example/ToTypescript.kt)
- some [kotlin](src/main/kotlin/com/example/OneDataType.kt) or [java](src/main/java/com/example/JavaClassTypes.java) classes
- add the plugin to the buildscript
- config the plugin input, output, and annotation reference

## Dev

```bash
./gradlew -b dev.build.gradle.kts build --stacktrace --rerun-tasks
```

```gradle
plugins {
    id("se.jensim.kt2ts") version "$pluginVersion"
}
kt2ts {
    output {
        outputFile = file("$buildDir/ts/kt2ts.d.ts")
        annotations = listOf("com.example.ToTypescript")
    }
    classFilesSources {
        compileTasks = listOf(tasks.compileKotlin, tasks.compileJava)
        classesDirs = files("$buildDir/classes/kotlin/main")
    }
}
// Shorthand version - with default values and single annotation->output
// kt2ts.annotation = "com.example.ToTypescript"
```
