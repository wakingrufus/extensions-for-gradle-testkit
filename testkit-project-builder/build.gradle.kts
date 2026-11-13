plugins {
    id("testkit.project")
    id("testkit.kotlin")
}
description = "custom assertions for testkit BuildResult and TaskResult"
dependencies {
    compileOnly(gradleTestKit())
    api("org.jspecify:jspecify:1.0.0")
    api(project(":testkit-runner"))

    testImplementation(gradleTestKit())
    testImplementation(project(":testkit-assertions"))
}