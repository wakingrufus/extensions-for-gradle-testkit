import org.gradle.kotlin.dsl.jacoco
import org.gradle.kotlin.dsl.`java-library`
import org.gradle.kotlin.dsl.repositories

plugins {
    `java-library`
    jacoco
}
repositories {
    mavenCentral()
}
dependencies {
    compileOnly(gradleTestKit())
    api("org.jspecify:jspecify:1.0.0")
    api("org.assertj:assertj-core:3.27.7")

    testImplementation(gradleTestKit())
}
testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter()
        }
    }
}
project.tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
        xml.required = true
    }
}
project.tasks.named("test") {
    finalizedBy("jacocoTestReport")
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}