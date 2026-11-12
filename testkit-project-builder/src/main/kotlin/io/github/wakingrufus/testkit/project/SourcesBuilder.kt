package io.github.wakingrufus.testkit.project

import java.io.File

/**
 * Represents the "src" directory of a project
 */
class SourcesBuilder internal constructor(private val sourcesDir: File) {
    fun sourceSet(name: String): SourceSetBuilder {
        return SourceSetBuilder(sourcesDir.toPath().resolve(name).toFile())
    }

    fun main(): SourceSetBuilder {
        return sourceSet("main")
    }

    fun test(): SourceSetBuilder {
        return sourceSet("test")
    }

    /**
     * Create source files in a custom source set
     * @param name name of the source set
     * @param config closure for configuration of the source set
     */
    fun sourceSet(name: String, config: SourceSetBuilder.() -> Unit) {
        sourceSet(name).apply(config)
    }

    fun main(config: SourceSetBuilder.() -> Unit) {
        main().apply(config)
    }

    fun test(config: SourceSetBuilder.() -> Unit) {
        test().apply(config)
    }
}
