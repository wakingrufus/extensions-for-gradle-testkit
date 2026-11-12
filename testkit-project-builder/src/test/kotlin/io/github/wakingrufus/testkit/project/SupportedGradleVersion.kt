package io.github.wakingrufus.testkit.project;

import io.github.wakingrufus.testkit.runner.Gradle;
import org.jspecify.annotations.NullMarked;

enum class SupportedGradleVersion(val version: Gradle) {
    MIN(Gradle.ofVersion("9.2.1")), CURRENT(Gradle.current());
}
