package io.github.wakingrufus.testkit.project

@TestKitDsl
class RepositoriesBuilder {
    private val repositories: MutableList<Repository> = mutableListOf()

    fun hasContent(): Boolean {
        return repositories.isNotEmpty()
    }

    /**
     * Declare a maven repository using a URL
     *
     * @param url the url of the repository
     */
    fun maven(url: String) {
        repositories.add(Maven(url))
    }

    /**
     * The built-in mavenCentral repository
     */
    fun mavenCentral() {
        repositories.add(BuiltIn("mavenCentral()"))
    }

    /**
     * The built-in mavenLocal repository
     */
    fun mavenLocal() {
        repositories.add(BuiltIn("mavenLocal()"))
    }

    internal fun build(buildscriptLanguage: BuildscriptLanguage?, baseIndentation: Int): String {
        return buildString {
            if (hasContent()) {
                append(" ".repeat(baseIndentation)).append("repositories {\n")
                for (repository in repositories) {
                    if (repository is BuiltIn) {
                        append(" ".repeat(baseIndentation)).append("    ").append(repository.functionName).append("\n")
                    } else if (repository is Maven) {
                        if (buildscriptLanguage === BuildscriptLanguage.GROOVY) {
                            append(" ".repeat(baseIndentation + 4)).append("maven {").append("\n")
                                .append(" ".repeat(baseIndentation + 8)).append("url = '")
                                .append(repository.url).append("'").append("\n")
                                .append(" ".repeat(baseIndentation)).append("    }").append("\n")
                        } else if (buildscriptLanguage === BuildscriptLanguage.KOTLIN) {
                            append(" ".repeat(baseIndentation)).append("    ").append("maven(")
                                .append("url = \"").append(repository.url).append("\"").append(")")
                                .append("\n")
                        }
                    }
                }
                append(" ".repeat(baseIndentation)).append("}\n")
            }
        }
    }
}