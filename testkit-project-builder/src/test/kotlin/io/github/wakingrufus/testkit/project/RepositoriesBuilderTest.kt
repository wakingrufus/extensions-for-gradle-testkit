package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RepositoriesBuilderTest {

    @Test
    fun testGroovyMaven() {
        val builder = RepositoriesBuilder()
        builder.maven("url")
        val actual = builder.build(BuildscriptLanguage.GROOVY, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """repositories {
    maven {
        url = 'url'
    }
}
"""
        )
    }

    @Test
    fun testGroovyMavenIndent() {
        val builder = RepositoriesBuilder()
        builder.maven("url")
        val actual = builder.build(BuildscriptLanguage.GROOVY, 4)
        //language=groovy
        assertThat(actual).isEqualTo(
            """    repositories {
        maven {
            url = 'url'
        }
    }
"""
        )
    }

    @Test
    fun testGroovyMavenCentral() {
        val builder = RepositoriesBuilder()
        builder.mavenCentral()
        val actual = builder.build(BuildscriptLanguage.GROOVY, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """repositories {
    mavenCentral()
}
"""
        )
    }

    @Test
    fun testKotlinMavenLocal() {
        val builder = RepositoriesBuilder()
        builder.mavenLocal()
        val actual = builder.build(BuildscriptLanguage.KOTLIN, 0)
        //language=groovy
        assertThat(actual).isEqualTo(
            """repositories {
    mavenLocal()
}
"""
        )
    }

    @Test
    fun testKotlinMaven() {
        val builder = RepositoriesBuilder()
        builder.maven("url")
        val actual = builder.build(BuildscriptLanguage.KOTLIN, 0)
        //language=kotlin
        assertThat(actual).isEqualTo(
            """repositories {
    maven(url = "url")
}
"""
        )
    }
}
