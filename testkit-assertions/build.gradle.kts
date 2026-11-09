plugins {
    id("testkit.project")
}
description = "custom assertions for testkit BuildResult and TaskResult"
dependencies {
    compileOnly(gradleTestKit())
    api("org.jspecify:jspecify:1.0.0")
    api("org.assertj:assertj-core:3.27.7")

    testImplementation(gradleTestKit())
}