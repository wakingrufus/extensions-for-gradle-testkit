package io.github.wakingrufus.testkit.project

@TestKitDsl
class Plugin internal constructor(private val id: String) {
    private var builtIn: String? = null
    private var builtInParam: String? = null
    private var version: String? = null
    private var apply: Boolean? = null

    /**
     * Set the version of the plugin.
     * This only needs to be called for plugins not already on the classpath
     *
     * @param version the version of the plugin
     */
    infix fun version(version: String): Plugin {
        this.version = version
        return this
    }

    fun builtIn(builtInName: String): Plugin {
        builtIn = builtInName
        return this
    }

    /**
     * Set apply = false to skip plugin application on a project
     */
    fun apply(apply: Boolean): Plugin {
        this.apply = apply
        return this
    }

    fun builtInParam(builtInParamName: String): Plugin {
        builtInParam = builtInParamName
        return this
    }

    internal fun render(language: BuildscriptLanguage): String {
        val stringBuilder = StringBuilder()

        if (language === BuildscriptLanguage.GROOVY) {
            stringBuilder.append("id '").append(id).append("'")
        } else if (language === BuildscriptLanguage.KOTLIN) {
            if (builtIn != null) {
                if (builtInParam == null) {
                    if (builtIn!!.contains("-")) {
                        stringBuilder.append("`").append(builtIn).append("`")
                    } else {
                        stringBuilder.append(builtIn)
                    }
                } else {
                    stringBuilder.append(builtIn).append("(\"").append(builtInParam).append("\")")
                }
            } else {
                stringBuilder.append("id(\"").append(id).append("\")")
            }
        }

        if (version != null) {
            if (language === BuildscriptLanguage.GROOVY) {
                stringBuilder.append(" version '").append(version).append("'")
            } else if (language === BuildscriptLanguage.KOTLIN) {
                stringBuilder.append(" version (\"").append(version).append("\")")
            }
        }

        if (apply != null) {
            if (language === BuildscriptLanguage.GROOVY) {
                stringBuilder.append(" apply ").append(apply)
            } else if (language === BuildscriptLanguage.KOTLIN) {
                stringBuilder.append(" apply (").append(apply).append(")")
            }
        }
        return stringBuilder.toString()
    }
}
