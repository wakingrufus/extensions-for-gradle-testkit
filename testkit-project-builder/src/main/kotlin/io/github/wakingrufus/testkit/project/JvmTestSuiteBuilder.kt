package io.github.wakingrufus.testkit.project

@TestKitDsl
class JvmTestSuiteBuilder {
    private var framework: String? = null

    /**
     * Enables Jupiter on the test suite (w/ dependencies)
     */
    fun useJUnitJupiter() {
        framework = "useJUnitJupiter()"
    }

    internal fun build(buildscriptLanguage: BuildscriptLanguage?, baseIndentation: Int): String {
        return buildString {
            if (framework != null) {
                append(" ".repeat(baseIndentation))
                append(framework)
                append("\n")
            }
        }
    }
}
