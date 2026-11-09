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