package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PluginsBuilderTest {

    @Test
    fun testGroovyId() {
        val builder = PluginsBuilder()
        builder.id("java")
        val actual = builder.build(BuildscriptLanguage.GROOVY, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """plugins {
    id 'java'
}
"""
        )
    }

    @Test
    fun testGroovyIdIndent() {
        val builder = PluginsBuilder()
        builder.id("java")
        val actual = builder.build(BuildscriptLanguage.GROOVY, 4)
        //language=groovy
        assertThat(actual).isEqualTo(
            """    plugins {
        id 'java'
    }
"""
        )
    }


    @Test
    fun testGroovyJava() {
        val builder = PluginsBuilder()
        builder.java()
        val actual = builder.build(BuildscriptLanguage.GROOVY, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """plugins {
    id 'java'
}
"""
        )
    }


    @Test
    fun testKotlinJava() {
        val builder = PluginsBuilder()
        builder.java()
        val actual = builder.build(BuildscriptLanguage.KOTLIN, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """plugins {
    java
}
"""
        )
    }

    @Test
    fun testKotlinIdWithVersion() {
        val builder = PluginsBuilder()
        builder.id("fake.id").version("0.0.0")
        val actual = builder.build(BuildscriptLanguage.KOTLIN, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """plugins {
    id("fake.id") version ("0.0.0")
}
"""
        )
    }

    @Test
    fun testKotlinJvm_kotlin() {
        val builder = PluginsBuilder()
        builder.kotlin("jvm").version("0.0.0")
        val actual = builder.build(BuildscriptLanguage.KOTLIN, 0)
        //language=kotlin
        assertThat(actual).isEqualTo(
            """plugins {
    kotlin("jvm") version ("0.0.0")
}
"""
        )
    }


    @Test
    fun testKotlinJvm_groovy() {
        val builder = PluginsBuilder()
        builder.kotlin("jvm").version("0.0.0")
        val actual = builder.build(BuildscriptLanguage.GROOVY, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """plugins {
    id 'org.jetbrains.kotlin.jvm' version '0.0.0'
}
"""
        )
    }

    @Test
    fun testKotlinDsl_kotlin() {
        val builder = PluginsBuilder()
        builder.kotlinDsl()
        val actual = builder.build(BuildscriptLanguage.KOTLIN, 0)
        //language=kotlin
        assertThat(actual).isEqualTo(
            """
                plugins {
                    `kotlin-dsl`
                }
                
                """.trimIndent()
        )
    }

    @Test
    fun testKotlinDsl_groovy() {
        val builder = PluginsBuilder()
        builder.kotlinDsl().version("0.0.0")
        val actual = builder.build(BuildscriptLanguage.GROOVY, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """
                plugins {
                    id 'org.gradle.kotlin.kotlin-dsl' version '0.0.0'
                }
                
                """.trimIndent()
        )
    }
}