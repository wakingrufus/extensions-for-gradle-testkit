package io.github.wakingrufus.testkit.project

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class ProjectBuilderTest {
    @Test
    fun testJavaPlugin(@TempDir testProjectDir: File) {
        val instance = ProjectBuilder(testProjectDir)
        instance.plugins().java()
        instance.build(BuildscriptLanguage.KOTLIN)

        assertThat(testProjectDir.toPath().resolve("build.gradle.kts")).content()
            .contains(
                """plugins {
    java
}
"""
            )
    }

    @Test
    fun testPlugin(@TempDir testProjectDir: File) {
        val instance = ProjectBuilder(testProjectDir)
        instance.plugins().id("groovy")
        instance.build(BuildscriptLanguage.KOTLIN)

        assertThat(testProjectDir.toPath().resolve("build.gradle.kts")).content()
            .contains(
                """plugins {
    id("groovy")
}
"""
            )
    }

    @Test
    fun testDependencies(@TempDir testProjectDir: File) {
        val instance = ProjectBuilder(testProjectDir)
        instance.dependencies {
            testImplementation("org.assertj:assertj-core:3.27.3")
        }
        instance.build(BuildscriptLanguage.KOTLIN)

        assertThat(testProjectDir.toPath().resolve("build.gradle.kts")).content()
            .contains(
                """dependencies {
    testImplementation("org.assertj:assertj-core:3.27.3")
}
"""
            )
    }

    @Test
    fun testSourceSet(@TempDir testProjectDir: File) {
        val instance = ProjectBuilder(testProjectDir)
        instance.src().sourceSet("integTest").java(
            "netflix/Test.java",  // language=java
            """package netflix;
class Test {
}
"""
        )
        instance.build(BuildscriptLanguage.KOTLIN)

        assertThat(testProjectDir.toPath().resolve("src/integTest/java/netflix/Test.java")).exists()
    }

    @Test
    fun testGroup(@TempDir testProjectDir: File) {
        val instance = ProjectBuilder(testProjectDir)
        instance.group("sample.group")
        instance.build(BuildscriptLanguage.KOTLIN)
        assertThat(testProjectDir.toPath().resolve("build.gradle.kts"))
            .exists()
            .content()
            .contains(
                """group = "sample.group"
"""
            )
    }

    @Test
    fun testVersion(@TempDir testProjectDir: File) {
        val instance = ProjectBuilder(testProjectDir)
        instance.version("1.0.0")
        instance.build(BuildscriptLanguage.KOTLIN)
        assertThat(testProjectDir.toPath().resolve("build.gradle.kts"))
            .exists()
            .content()
            .contains(
                """version = "1.0.0"
"""
            )
    }
}
