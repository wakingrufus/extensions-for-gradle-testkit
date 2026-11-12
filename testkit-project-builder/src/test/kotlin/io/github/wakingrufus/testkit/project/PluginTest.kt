package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class PluginTest {

    @Test
    fun test_id_java() {
        val actual = Plugin("java").render(BuildscriptLanguage.GROOVY)
        Assertions.assertThat(actual).isEqualTo("id 'java'")
    }

    @Test
    fun test_kotlin_apply_false_kotlin() {
        val actual = Plugin("org.jetbrains.kotlin.jvm")
            .builtIn("kotlin")
            .builtInParam("jvm")
            .apply(false)
            .render(BuildscriptLanguage.KOTLIN)
        Assertions.assertThat(actual).isEqualTo("kotlin(\"jvm\") apply (false)")
    }

    @Test
    fun test_kotlin_version_apply_false_kotlin() {
        val actual = Plugin("org.jetbrains.kotlin.jvm")
            .builtIn("kotlin")
            .builtInParam("jvm")
            .version("2.4.10")
            .apply(false)
            .render(BuildscriptLanguage.KOTLIN)
        Assertions.assertThat(actual).isEqualTo("kotlin(\"jvm\") version (\"2.4.10\") apply (false)")
    }

    @Test
    fun test_kotlin_apply_false_groovy() {
        val actual = Plugin("org.jetbrains.kotlin.jvm")
            .builtIn("kotlin")
            .builtInParam("jvm")
            .apply(false)
            .render(BuildscriptLanguage.GROOVY)
        Assertions.assertThat(actual).isEqualTo("id 'org.jetbrains.kotlin.jvm' apply false")
    }
}