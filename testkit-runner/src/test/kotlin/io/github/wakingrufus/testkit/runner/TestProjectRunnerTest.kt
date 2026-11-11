package io.github.wakingrufus.testkit.runner;

import io.github.wakingrufus.testkit.assertions.TestKitAssertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.io.File
import java.nio.file.Files

internal class TestProjectRunnerTest {
    @TempDir
    lateinit var testProjectDir: File

    @ParameterizedTest
    @EnumSource(SupportedGradleVersion::class)
    fun testDeprecations(gradleVersion: SupportedGradleVersion) {
        val buildFile = testProjectDir.toPath().resolve("build.gradle.kts");
        Files.writeString(
            buildFile, "plugins {\n" +
                    "   id(\"java\")\n" +
                    "}" +
                    " open class Deprecate : DefaultTask() {\n" +
                    "                    @TaskAction\n" +
                    "                    fun action() {\n" +
                    "                        this.project.logger.lifecycle(\"deprecated project usage\")\n" +
                    "                    }\n" +
                    "                }\n" +
                    "                tasks.create<Deprecate>(\"deprecate\") {\n" +
                    "                }"
        );

        val runner = TestProjectRunner(testProjectDir)
        val result = runner.run("deprecate", withPluginClasspath = false) {
            forwardOutput()
            withGradle(gradleVersion.version)
        }

        assertThatCode { assertThat(result).hasNoDeprecationWarnings() }
            .isInstanceOf(AssertionError::class.java)
        assertThat(result.output)
            .`as`("warning mode defaults to all")
            .contains("Invocation of Task.project at execution time has been deprecated. ")
    }
}
