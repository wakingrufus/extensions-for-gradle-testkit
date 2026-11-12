package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class SettingsBuilderTest {
    @TempDir
    lateinit var testDir: File

    @Test
    fun testGroovy() {
        val builder = SettingsBuilder(testDir)
        builder.includeProject("sub")
        builder.build(BuildscriptLanguage.GROOVY)
        Assertions.assertThat(testDir.toPath().resolve("settings.gradle").toFile()).exists()
            .content().isEqualTo(
                """include ':sub'
"""
            )
    }

    @Test
    fun testKotlin() {
        val builder = SettingsBuilder(testDir)
        builder.includeProject("sub")
        builder.build(BuildscriptLanguage.KOTLIN)
        Assertions.assertThat(testDir.toPath().resolve("settings.gradle.kts").toFile()).exists()
            .content().isEqualTo(
                """include(":sub")
"""
            )
    }

    @Test
    fun test_custom_path() {
        val builder = SettingsBuilder(testDir)
        builder.includeProject("sub", "custom")
        builder.build(BuildscriptLanguage.KOTLIN)
        Assertions.assertThat(testDir.toPath().resolve("settings.gradle.kts").toFile()).exists()
            .content().isEqualTo(
                """include(":sub")
project(":sub").projectDir = file("custom")
"""
            )
    }

    @Test
    fun test_nested_path() {
        val builder = SettingsBuilder(testDir)
        builder.includeProject("group:sub")
        builder.build(BuildscriptLanguage.KOTLIN)
        Assertions.assertThat(testDir.toPath().resolve("settings.gradle.kts").toFile()).exists()
            .content().isEqualTo(
                """include(":group:sub")
"""
            )
    }
}