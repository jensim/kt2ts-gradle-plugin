package se.jensim.gradle.plugin.kt2ts

import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class FuncTest {

    @JvmField
    @Rule
    val tempFolder = TemporaryFolder()

    @Ignore("This test depends on published version of the gradle plugin and therefore cannot be trusted entirely any breaking change will make this test fail.")
    @Test
    fun `func test plugin portal version`() {
        // given
        setUp("build.gradle.kts")

        // when
        run("build")

        // then
        verifyOutput()
    }

    @Ignore("Only works after publish to local maven repo")
    @Test
    fun `func test local version`() {
        // given
        setUp("dev.build.gradle.kts")

        //when
        run("build") {
            withPluginClasspath()
        }

        //then
        verifyOutput()
    }

    private fun setUp(gradleFile: String) {
        val src = tempFolder.newFolder("src")
        File("example/src").copyRecursively(src)
        val buildFile = tempFolder.newFile("build.gradle.kts")
        buildFile.writeText(File("example/$gradleFile").readText())
    }

    private fun run(vararg arguments: String, additionalConfig: GradleRunner.() -> Unit = {}) {
        GradleRunner.create().apply {
            withProjectDir(tempFolder.root)
            withArguments(arguments.asList().plus(listOf("--stacktrace", "--info")))
            additionalConfig()
        }.build()
    }

    private fun verifyOutput() {
        val output = File(tempFolder.root, "build/ts/kt2ts.d.ts")
        assertTrue(output.exists())
        val readText = output.readText()
        assertThat(readText.length, greaterThan(0))
    }
}
