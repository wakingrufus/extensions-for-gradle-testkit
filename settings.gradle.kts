pluginManagement {
    includeBuild("build-logic")
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "extensions-for-gradle-testkit"

gradle.beforeProject {
    group = "io.github.wakingrufus"
}
include("testkit-assertions")
include("testkit-project-builder")
include("testkit-runner")