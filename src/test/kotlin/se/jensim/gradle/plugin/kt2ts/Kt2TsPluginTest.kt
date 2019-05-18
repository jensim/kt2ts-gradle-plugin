package se.jensim.gradle.plugin.kt2ts

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Test

class Kt2TsPluginTest  {

    @Test
    fun `create project`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(Kt2TsPlugin::class.java)

        val extension = project.extensions.getByType(Kt2TsPluginExtension::class.java)
        Assert.assertNotNull(extension)

        val task = project.tasks.findByName("kt2ts")
        Assert.assertNotNull(task)
    }
}
