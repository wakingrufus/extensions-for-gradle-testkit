package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class TestingSuitesBuilderTest {
    @Test
    fun test_create_custom_suite_groovy() {
        val instance = TestingSuitesBuilder()
        instance.create("custom")
        val actual = instance.build(BuildscriptLanguage.GROOVY, 0)
        assertThat(actual).isEqualTo( //language=groovy
            """suites {
    custom(JvmTestSuite) {
    }
}
"""
        )
    }
}
