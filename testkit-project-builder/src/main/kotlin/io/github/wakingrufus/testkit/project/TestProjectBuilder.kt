package io.github.wakingrufus.testkit.project

import io.github.wakingrufus.testkit.runner.TestProjectRunner
import java.io.File
import java.util.function.Consumer

/**
 * This class is the entry point into the "test project builder" API.
 * Although it may be used from Java, it is most effective when used via the Groovy or Kotlin DSLs.
 * See {@link GroovyTestProjectBuilder} and {@link KotlinTestProjectBuilderKt} for more information on the DSLs.
 */
@TestKitDsl
class TestProjectBuilder private constructor(private val projectDir: File) {
    companion object {
        @JvmStatic
        fun testProject(testProjectDir: File): TestProjectBuilder {
            return TestProjectBuilder(testProjectDir)
        }
    }
    private val rootProject  = ProjectBuilder(projectDir)
    private val subProjects: MutableMap<String?, ProjectBuilder> = mutableMapOf()
    private val includedBuilds: MutableMap<String?, TestProjectBuilder?> = mutableMapOf()
    private val settings = SettingsBuilder(projectDir)
    private val properties = ProjectPropertiesBuilder(projectDir)

    /**
     * configure the root project of the build
     *
     * @return builder for the root project
     */
    fun rootProject(): ProjectBuilder {
        return rootProject
    }

    fun rootProject(config: ProjectBuilder.() -> Unit) {
        rootProject().apply(config)
    }

    fun properties(): ProjectPropertiesBuilder {
        return properties
    }

    fun properties(config: ProjectPropertiesBuilder.() -> Unit) {
        properties().apply(config)
    }

    /**
     * configure the settings (settings.gradle.kts)
     *
     * @return builder for the settings
     */
    fun settings(): SettingsBuilder {
        return settings
    }

    fun settings(config: SettingsBuilder.() -> Unit) {
        settings().apply(config)
    }

    fun subProject(name: String): ProjectBuilder {
        return subProject(name, null)
    }

    fun subProject(name: String, path: String? = null, config: ProjectBuilder.() -> Unit) {
        subProject(name, path).apply(config)
    }

    private fun convertProjectNameToDefaultPath(name: String): String {
        var namePathConvention = name
        if (namePathConvention.startsWith(":")) {
            namePathConvention = namePathConvention.substring(1)
        }
        namePathConvention = namePathConvention.replace(":", "/")
        return namePathConvention
    }

    fun subProject(name: String, path: String?): ProjectBuilder {
        val projectPath = if (path == null) convertProjectNameToDefaultPath(name) else path
        val subProjectDir = projectDir.toPath().resolve(projectPath).toFile()
        subProjectDir.mkdirs()
        val project: ProjectBuilder = ProjectBuilder(subProjectDir)
        subProjects.put(name, project)
        settings.includeProject(name, path)
        return project
    }

    fun includedBuild(name: String): TestProjectBuilder {
        val projectPath = convertProjectNameToDefaultPath(name)
        val subProjectDir = projectDir.toPath().resolve(projectPath).toFile()
        subProjectDir.mkdirs()
        val includedBuild = TestProjectBuilder(subProjectDir)
        includedBuilds.put(name, includedBuild)
        settings.includeBuild(name)
        return includedBuild
    }

    fun includedBuild(name: String, config: TestProjectBuilder.() -> Unit) {
        includedBuild(name).apply(config)
    }

    fun includedPluginBuild(name: String): TestProjectBuilder {
        val projectPath = convertProjectNameToDefaultPath(name)
        val subProjectDir = projectDir.toPath().resolve(projectPath).toFile()
        subProjectDir.mkdirs()
        val includedBuild = TestProjectBuilder(subProjectDir)
        includedBuilds.put(name, includedBuild)
        settings.pluginManagement().includeBuild(name)
        return includedBuild
    }

    fun includedPluginBuild(name: String, config: TestProjectBuilder.() -> Unit) {
        includedPluginBuild(name).apply(config)
    }

    fun build(): TestProjectRunner {
        return build(BuildscriptLanguage.KOTLIN)
    }

    fun build(language: BuildscriptLanguage): TestProjectRunner {
        properties().build()
        settings.build(language)
        includedBuilds.values.forEach(Consumer { it: TestProjectBuilder -> it.build(language) })
        rootProject.build(language)
        subProjects.values.forEach(Consumer { subProject: ProjectBuilder -> subProject.build(language) })
        return TestProjectRunner(projectDir)
    }
}
