package io.github.wakingrufus.testkit.assertions;

import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.github.wakingrufus.testkit.assertions.TestKitAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class TestKitAssertionsTest {

    @Test
    public void testTaskResult(@TempDir File testProjectDir) throws IOException {
        final var buildFile = testProjectDir.toPath().resolve("build.gradle.kts");
        Files.writeString(buildFile, "plugins {\n" +
                "   id(\"java\")\n" +
                "}");

        final var result =  GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("build")
                .build();

        assertThat(result).hasNoDeprecationWarnings();
        assertThat(result).task(":compileJava").hasOutcome(TaskOutcome.NO_SOURCE);
        assertThat(result).task(":build").hasOutcome(TaskOutcome.SUCCESS);
    }

    @Test
    public void testTaskResultInvalid(@TempDir File testProjectDir) throws IOException {
        final var buildFile = testProjectDir.toPath().resolve("build.gradle.kts");
        Files.writeString(buildFile, "plugins {\n" +
                "   id(\"java\")\n" +
                "}");

        final var result =  GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("build")
                .build();

        assertThatCode(() -> assertThat(result).task(":compileJava2").hasOutcome(TaskOutcome.NO_SOURCE))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    public void testDeprecations(@TempDir File testProjectDir) throws IOException {
        final var buildFile = testProjectDir.toPath().resolve("build.gradle.kts");
        Files.writeString(buildFile, "plugins {\n" +
                "   id(\"java\")\n" +
                "}" +
                " open class Deprecate : DefaultTask() {\n" +
                "                    @TaskAction\n" +
                "                    fun action() {\n" +
                "                        this.project.logger.lifecycle(\"deprecated project usage\")\n" +
                "                    }\n" +
                "                }\n" +
                "                tasks.create<Deprecate>(\"deprecate\") {\n" +
                "                }");

        final var result =  GradleRunner.create()
                .withProjectDir(testProjectDir)
                .forwardOutput()
                .withArguments("deprecate")
                .build();

        assertThatCode(() -> assertThat(result).hasNoDeprecationWarnings())
                .isInstanceOf(AssertionError.class);
    }
}
