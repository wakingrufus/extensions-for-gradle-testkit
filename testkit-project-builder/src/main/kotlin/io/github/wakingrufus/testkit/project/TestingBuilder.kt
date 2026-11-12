package io.github.wakingrufus.testkit.project

@TestKitDsl
class TestingBuilder {
    private val suites: TestingSuitesBuilder = TestingSuitesBuilder()

    fun suites(): TestingSuitesBuilder {
        return suites
    }
    fun suites(config: TestingSuitesBuilder.() -> Unit) {
        suites().apply(config)
    }

    fun hasContent(): Boolean {
        return suites.hasContent()
    }

    internal fun build(buildscriptLanguage: BuildscriptLanguage, baseIndentation: Int): String {
        return buildString {
            if (hasContent()) {
                append(" ".repeat(baseIndentation)).append("testing {\n")
                append(suites.build(buildscriptLanguage, baseIndentation + 4))
                append(" ".repeat(baseIndentation)).append("}\n")
            }
        }
    }
}
