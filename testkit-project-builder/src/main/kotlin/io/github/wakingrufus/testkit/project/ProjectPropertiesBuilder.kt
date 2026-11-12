package io.github.wakingrufus.testkit.project

import org.jetbrains.annotations.Contract
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*

@TestKitDsl
class ProjectPropertiesBuilder(private val projectDir: File) {
    private val properties: MutableList<String> = mutableListOf()

    @Contract("_,_ -> this")
    fun property(property: String, value: String): ProjectPropertiesBuilder {
        properties.add("$property=$value")
        return this
    }

    @Contract("_ -> this")
    fun buildCache(enabled: Boolean): ProjectPropertiesBuilder {
        return property("org.gradle.caching", enabled.toString().lowercase(Locale.getDefault()))
    }

    @Contract("_ -> this")
    fun configurationCache(enabled: Boolean): ProjectPropertiesBuilder {
        return property("org.gradle.configuration-cache", enabled.toString().lowercase(Locale.getDefault()))
    }

    /**
     * Shortcut for enabling [isolated projects](https://docs.gradle.org/current/userguide/isolated_projects.html).
     * This sets both the official `org.gradle.isolated-projects` property and the property used in Gradle prior to 9.7 for the incubating feature `org.gradle.unsafe.isolated-projects`
     */
    @Contract("_ -> this")
    fun isolatedProjects(enabled: Boolean): ProjectPropertiesBuilder {
        property("org.gradle.isolated-projects", enabled.toString().lowercase(Locale.getDefault()))
        property("org.gradle.unsafe.isolated-projects", enabled.toString().lowercase(Locale.getDefault()))
        return this
    }

    internal fun build() {
        val propsFile = projectDir.toPath().resolve("gradle.properties")
        val fileContents = properties.joinToString("\n")
        try {
            Files.write(propsFile, fileContents.toByteArray(StandardCharsets.UTF_8))
        } catch (e: IOException) {
            throw RuntimeException("Error writing properties file" + propsFile.toFile().absolutePath, e)
        }
    }
}
