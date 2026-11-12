package io.github.wakingrufus.testkit.project

import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.function.Consumer

@TestKitDsl
class SettingsBuilder internal constructor(private val projectDir: File) {
    private val pluginManagement = PluginManagementBuilder()
    private val plugins = PluginsBuilder()
    private var rawSettingsScript: String? = null
    private var name: String? = null
    private val projects: MutableSet<SubProject> = mutableSetOf()
    private val includedBuilds: MutableSet<SubProject> = mutableSetOf()

    fun name(name: String?) {
        this.name = name
    }

    fun pluginManagement(): PluginManagementBuilder {
        return pluginManagement
    }

    fun pluginManagement(config: PluginManagementBuilder.() -> Unit) {
        pluginManagement().apply(config)
    }

    fun plugins(): PluginsBuilder {
        return plugins
    }

    fun plugins(config: PluginsBuilder.() -> Unit) {
        plugins().apply(config)
    }

    /**
     * add a project include statement along with a projectDir override.
     * This is invoked automatically when adding projects via [TestProjectBuilder.subProject]
     *
     * @param name the name of the project to include
     */
    /**
     * add a project include statement.
     * This is invoked automatically when adding projects via [TestProjectBuilder.subProject]
     *
     * @param name the name of the project to include
     */
    @JvmOverloads
    fun includeProject(name: String, relativePath: String? = null) {
        projects.add(SubProject(name, relativePath))
    }

    /**
     * add a build to this build as a [composite build](https://docs.gradle.org/current/userguide/composite_builds.html)
     * include statement along with a projectDir override.
     * This is invoked automatically when adding projects via [TestProjectBuilder.includedBuild]
     *
     * @param name the name of the project to include
     */
    fun includeBuild(name: String, relativePath: String?) {
        includedBuilds.add(SubProject(name, relativePath))
    }

    fun includeBuild(name: String) {
        includedBuilds.add(SubProject(name, null))
    }

    fun rawSettingsScript(settingsScript: String) {
        rawSettingsScript = settingsScript
    }

    fun build(language: BuildscriptLanguage) {
        val textBuilder = StringBuilder()
        textBuilder.append(pluginManagement.build(language))
        textBuilder.append(plugins.build(language, 0))
        if (name != null) {
            textBuilder.append("rootProject.name = \"").append(name).append("\"\n")
        }
        if (rawSettingsScript != null) {
            textBuilder.append(rawSettingsScript)
        }
        includedBuilds.forEach(Consumer { includedBuild: SubProject ->
            if (language === BuildscriptLanguage.KOTLIN) {
                textBuilder.append("includeBuild(\"").append(includedBuild.name()).append("\")\n")
            } else if (language === BuildscriptLanguage.GROOVY) {
                textBuilder.append("includeBuild '").append(includedBuild.name()).append("'\n")
            }
        })
        projects.forEach(Consumer { subProject: SubProject ->
            val fqn = ":" + subProject.name()
            if (language === BuildscriptLanguage.KOTLIN) {
                textBuilder.append("include(\"").append(fqn).append("\")\n")
            } else if (language === BuildscriptLanguage.GROOVY) {
                textBuilder.append("include '").append(fqn).append("'\n")
            }
            if (subProject.relativePath() != null) {
                textBuilder.append("project(\"")
                    .append(fqn)
                    .append("\").projectDir = file(\"")
                    .append(subProject.relativePath())
                    .append("\")\n")
            }
        })
        val ext = if (language === BuildscriptLanguage.GROOVY) "gradle" else "gradle.kts"
        val settingsFile = projectDir.toPath().resolve("settings.$ext")
        try {
            Files.write(settingsFile, textBuilder.toString().toByteArray(StandardCharsets.UTF_8))
        } catch (e: IOException) {
            throw RuntimeException("Error writing to " + settingsFile.toAbsolutePath(), e)
        }
    }
}
