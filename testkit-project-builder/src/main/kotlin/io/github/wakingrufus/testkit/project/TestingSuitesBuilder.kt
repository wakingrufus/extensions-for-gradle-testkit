package io.github.wakingrufus.testkit.project

@TestKitDsl
class TestingSuitesBuilder {
    private val existingSuites: MutableMap<String, JvmTestSuiteBuilder> = mutableMapOf()
    private val createdSuites: MutableMap<String, JvmTestSuiteBuilder> = mutableMapOf()

    fun hasContent(): Boolean {
        return existingSuites.isNotEmpty() || createdSuites.isNotEmpty()
    }

    fun test(): JvmTestSuiteBuilder {
        return named("test")
    }

    fun test(config: JvmTestSuiteBuilder.() -> Unit) {
        test().apply(config)
    }

    fun create(name: String, config: JvmTestSuiteBuilder.() -> Unit) {
        create(name).apply(config)
    }

    fun named(name: String, config: JvmTestSuiteBuilder.() -> Unit) {
        named(name).apply(config)
    }
    fun create(name: String): JvmTestSuiteBuilder {
        return createdSuites.computeIfAbsent(name) { JvmTestSuiteBuilder() }
    }

    fun named(name: String): JvmTestSuiteBuilder {
        return existingSuites.computeIfAbsent(name) { JvmTestSuiteBuilder() }
    }

    internal fun build(buildscriptLanguage: BuildscriptLanguage, baseIndentation: Int): String {
        val stringBuilder = StringBuilder()
        if (hasContent()) {
            stringBuilder.append(" ".repeat(baseIndentation)).append("suites {\n")
            if (!existingSuites.isEmpty()) {
                existingSuites.forEach { (key: String?, value: JvmTestSuiteBuilder) ->
                    stringBuilder.append(" ".repeat(baseIndentation + 4))
                    when (buildscriptLanguage) {
                        BuildscriptLanguage.GROOVY -> stringBuilder.append(key)
                        BuildscriptLanguage.KOTLIN -> stringBuilder.append("named<JvmTestSuite>(\"").append(key)
                            .append("\")")
                    }
                    stringBuilder.append(" {\n")
                    stringBuilder.append(value.build(buildscriptLanguage, baseIndentation + 8))
                    stringBuilder.append(" ".repeat(baseIndentation + 4)).append("}\n")
                }
            }
            if (createdSuites.isNotEmpty()) {
                createdSuites.forEach { (key: String?, value: JvmTestSuiteBuilder) ->
                    stringBuilder.append(" ".repeat(baseIndentation + 4))
                    when (buildscriptLanguage) {
                        BuildscriptLanguage.GROOVY -> stringBuilder.append("$key(JvmTestSuite)")
                        BuildscriptLanguage.KOTLIN -> stringBuilder.append("create<JvmTestSuite>(\"").append(key)
                            .append("\")")
                    }
                    stringBuilder.append(" {\n")
                    stringBuilder.append(value.build(buildscriptLanguage, baseIndentation + 8))
                    stringBuilder.append(" ".repeat(baseIndentation + 4)).append("}\n")
                }
            }
            stringBuilder.append(" ".repeat(baseIndentation)).append("}\n")
        }
        return stringBuilder.toString()
    }
}
