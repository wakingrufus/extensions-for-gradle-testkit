package io.github.wakingrufus.testkit.project

import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files

@TestKitDsl
class ProjectBuilder internal constructor(private val projectDir: File) {
    private val plugins = PluginsBuilder()
    private val repositoriesBuilder: RepositoriesBuilder = RepositoriesBuilder()
    private val buildscriptBuilder = BuildscriptBuilder()
    private val testingBuilder: TestingBuilder = TestingBuilder()
    private val sources = SourcesBuilder(projectDir.toPath().resolve("src").toFile())
    private val dependenciesBuilder = DependenciesBuilder()
    private var javaToolchain: Int? = null
    private val rawBuildScript: MutableList<String> = mutableListOf()
    private var group: String? = null
    private var version: String? = null

    fun plugins(): PluginsBuilder {
        return plugins
    }

    fun plugins(config: PluginsBuilder.() -> Unit) {
        plugins().apply(config)
    }

    fun repositories(): RepositoriesBuilder {
        return repositoriesBuilder
    }

    fun repositories(config: RepositoriesBuilder.() -> Unit) {
        repositories().apply(config)
    }

    fun buildscript(): BuildscriptBuilder {
        return buildscriptBuilder
    }

    fun buildscript(config: BuildscriptBuilder.() -> Unit) {
        buildscript().apply(config)
    }

    fun javaToolchain(javaToolchain: Int?) {
        this.javaToolchain = javaToolchain
    }

    fun dependencies(): DependenciesBuilder {
        return dependenciesBuilder
    }

    fun dependencies(config: DependenciesBuilder.() -> Unit) {
        dependencies().apply(config)
    }

    fun testing(): TestingBuilder {
        return testingBuilder
    }
    fun testing(config: TestingBuilder.() -> Unit) {
        testing().apply(config)
    }

    fun rawBuildScript(buildScript: String) {
        rawBuildScript.add(buildScript)
    }

    fun src(): SourcesBuilder {
        return sources
    }

    fun src(config: SourcesBuilder.() -> Unit) {
        src().apply(config)
    }

    /**
     * Set project group
     *
     * @param group group name to set on project
     */
    fun group(group: String?) {
        this.group = group
    }

    /**
     * Set project version, which is equivalent to passing -Pversion on the command line
     *
     * @param version group name to set on project
     */
    fun version(version: String?) {
        this.version = version
    }

    internal fun build(language: BuildscriptLanguage?) {
        val buildFileText = StringBuilder()
        buildFileText.append(buildscriptBuilder.build(language!!, 0))
        buildFileText.append(plugins.build(language, 0))
        if (group != null) {
            buildFileText.append("group = \"").append(group).append("\"\n")
        }
        if (version != null) {
            buildFileText.append("version = \"").append(version).append("\"\n")
        }
        buildFileText.append(repositoriesBuilder.build(language, 0))
        buildFileText.append(dependenciesBuilder.build(language, 0))
        if (javaToolchain != null) {
            buildFileText.append("java {\n    toolchain {\n")
            buildFileText.append("        languageVersion = JavaLanguageVersion.of(").append(javaToolchain)
                .append(")\n")
            buildFileText.append("    }\n}\n")
        }
        buildFileText.append(testingBuilder.build(language, 0))
        buildFileText.append(rawBuildScript.joinToString("\n"))
        val ext = if (language === BuildscriptLanguage.GROOVY) "gradle" else "gradle.kts"
        val buildFile = projectDir.toPath().resolve("build.$ext")
        try {
            Files.write(buildFile, buildFileText.toString().toByteArray(StandardCharsets.UTF_8))
        } catch (e: IOException) {
            throw RuntimeException("Error writing to " + buildFile.toAbsolutePath(), e)
        }
    }
}