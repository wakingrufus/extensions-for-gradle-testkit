package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class SourceSetBuilderTest {
    @TempDir
    private lateinit var tempDir: File

    @Test
    fun test_groovy() {
        val instance = SourceSetBuilder(tempDir)
        instance.groovy("test.groovy", "testGroovy")
        Assertions.assertThat(tempDir.toPath().resolve("groovy").resolve("test.groovy"))
            .exists()
            .hasContent("testGroovy")
    }

    @Test
    fun test_scala() {
        val instance = SourceSetBuilder(tempDir)
        instance.scala("Test.scala", "class ScalaClass")
        Assertions.assertThat(tempDir.toPath().resolve("scala").resolve("Test.scala"))
            .exists()
            .hasContent("class ScalaClass")
    }

    @Test
    fun test_resources() {
        val instance = SourceSetBuilder(tempDir)
        instance.resources("application.properties", "test=true")
        Assertions.assertThat(tempDir.toPath().resolve("resources").resolve("application.properties"))
            .exists()
            .hasContent("test=true")
    }
}