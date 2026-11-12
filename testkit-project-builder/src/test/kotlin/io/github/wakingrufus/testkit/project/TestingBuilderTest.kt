package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TestingBuilderTest {
    @Test
    fun test_groovy() {
        val instance = TestingBuilder()
        instance.suites().named("test").useJUnitJupiter()
        val actual = instance.build(BuildscriptLanguage.GROOVY, 0)
        assertThat(actual).isEqualTo(
            """testing {
    suites {
        test {
            useJUnitJupiter()
        }
    }
}
"""
        )
    }

    @Test
    fun test_kotlin() {
        val instance = TestingBuilder()
        instance.suites().named("test").useJUnitJupiter()
        val actual = instance.build(BuildscriptLanguage.KOTLIN, 0)
        assertThat(actual).isEqualTo(
            """testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}
"""
        )
    }
}