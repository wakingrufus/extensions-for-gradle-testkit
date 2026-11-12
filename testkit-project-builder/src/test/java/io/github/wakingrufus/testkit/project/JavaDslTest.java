package io.github.wakingrufus.testkit.project;

import io.github.wakingrufus.testkit.runner.Gradle;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.net.URI;

import static io.github.wakingrufus.testkit.assertions.TestKitAssertions.assertThat;

public class JavaDslTest {

    @ParameterizedTest
    @EnumSource(SupportedGradleVersion.class)
    public void testJavaDsl(SupportedGradleVersion gradleVersion, @TempDir File testProjectDir) {
        final var builder = TestProjectBuilder.testProject(testProjectDir);
        builder.properties().property("test", "value");
        builder.rootProject().plugins().java();
        final var runner = builder.build();
        GradleRunner gradleRunner = GradleRunner.create();
        if (gradleVersion.getVersion() instanceof Gradle.GradleVersion) {
            gradleRunner.withGradleVersion(((Gradle.GradleVersion) gradleVersion.getVersion()).version());
        } else if (gradleVersion.getVersion() instanceof Gradle.GradleDistribution) {
            gradleRunner.withGradleDistribution(URI.create(((Gradle.GradleDistribution) gradleVersion.getVersion()).url()));
        }
        final var result = runner.run(gradleRunner, "build");

        assertThat(result)
                .hasNoProblemsReport()
                .hasNoDeprecationWarnings()
                .hasNoMutableStateWarnings();
        assertThat(result).task(":compileJava").hasOutcome(TaskOutcome.NO_SOURCE);
        assertThat(result).task(":build").hasOutcome(TaskOutcome.SUCCESS);
        assertThat(testProjectDir.toPath().resolve("gradle.properties"))
                .exists()
                .content()
                .contains("test=value");
    }
}
