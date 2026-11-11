package io.github.wakingrufus.testkit.runner

import org.gradle.testkit.runner.GradleRunner
import java.net.URI

/**
 * Set the gradle distribution to use based on a [Gradle]
 */
fun GradleRunner.withGradle(gradleVersion: Gradle) {
    when (gradleVersion) {
        is Gradle.GradleVersion -> withGradleVersion(gradleVersion.version)
        is Gradle.GradleDistribution -> withGradleDistribution(URI.create(gradleVersion.url))
        else -> {}
    }
}