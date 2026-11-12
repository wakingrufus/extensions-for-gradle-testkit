package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class ProjectPropertiesBuilderTest {
    @Test
    fun testProjectProperty(@TempDir testProjectDir: File) {
        val instance = ProjectPropertiesBuilder(testProjectDir)
        instance.property("org.gradle.caching", "true")
        instance.build()

        assertThat(testProjectDir.toPath().resolve("gradle.properties"))
            .exists()
            .content()
            .contains("org.gradle.caching=true")
    }

    @Test
    fun testBuildCache(@TempDir testProjectDir: File) {
        val instance = ProjectPropertiesBuilder(testProjectDir)
        instance.buildCache(true)
        instance.build()

        assertThat(testProjectDir.toPath().resolve("gradle.properties"))
            .exists()
            .content()
            .contains("org.gradle.caching=true")
    }

    @Test
    fun testConfigurationCache(@TempDir testProjectDir: File) {
        val instance = ProjectPropertiesBuilder(testProjectDir)
        instance.configurationCache(true)
        instance.build()

        assertThat(testProjectDir.toPath().resolve("gradle.properties"))
            .exists()
            .content()
            .contains("org.gradle.configuration-cache=true")
    }

    @Test
    fun testIsolatedProjects(@TempDir testProjectDir: File) {
        val instance = ProjectPropertiesBuilder(testProjectDir)
        instance.isolatedProjects(true)
        instance.build()

        assertThat(testProjectDir.toPath().resolve("gradle.properties"))
            .exists()
            .content()
            .contains("org.gradle.unsafe.isolated-projects=true")
            .contains("org.gradle.isolated-projects=true")
    }
}