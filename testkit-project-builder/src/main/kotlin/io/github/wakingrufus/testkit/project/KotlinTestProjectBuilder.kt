package io.github.wakingrufus.testkit.project

import io.github.wakingrufus.testkit.runner.TestProjectRunner
import java.io.File

/**
 * Entry point for the Kotlin DSL for [TestProjectBuilder].
 * @param
 */
fun testProject(
    testProjectDir: File,
    language: BuildscriptLanguage = BuildscriptLanguage.KOTLIN,
    config: TestProjectBuilder.() -> Unit
): TestProjectRunner {
    val testProjectBuilder = TestProjectBuilder.testProject(testProjectDir)
    testProjectBuilder.apply(config)
    return testProjectBuilder.build(language)
}
