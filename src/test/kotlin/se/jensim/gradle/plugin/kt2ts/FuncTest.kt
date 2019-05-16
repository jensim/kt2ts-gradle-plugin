package se.jensim.gradle.plugin.kt2ts

import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class FuncTest {

    @JvmField
    @Rule
    val tempFolder = TemporaryFolder()

    //@Ignore("This test depends on published version of the gradle plugin and therefore cannot be trusted entirely
    // any breaking change will make this test fail.")
    @Test
    fun `func test plugin portal version`() {
        // given
        val src = tempFolder.newFolder("src")
        File("example/src").copyRecursively(src)
        val buildFile = tempFolder.newFile("build.gradle.kts")
        buildFile.writeText(File("example/build.gradle.kts").readText())

        // when
        val runner = GradleRunner.create().apply {
            withProjectDir(tempFolder.root)
            withArguments("build")
        }
        runner.build()

        // then
        val output = File(tempFolder.root, "build/ts/kt2ts.d.ts")
        Assert.assertTrue(output.exists())
        val readText = output.readText()
        assertThat(readText.length, greaterThan(0))
    }

    @Ignore("Only works after publish to local maven repo")
    @Test
    fun `func test local version`() {
        // given
        val src = tempFolder.newFolder("src")
        File("example/src").copyRecursively(src)
        val buildFile = tempFolder.newFile("build.gradle.kts")
        buildFile.writeText(File("example/dev.build.gradle.kts").readText())

        //when
        val runner = GradleRunner.create().apply {
            withProjectDir(tempFolder.root)
            withArguments("build")
            withPluginClasspath()
        }
        runner.build()

        //then
        val output = File(tempFolder.root, "build/ts/kt2ts.d.ts")
        Assert.assertTrue(output.exists())
        val readText = output.readText()
        assertThat(readText.length, greaterThan(0))
    }
}
