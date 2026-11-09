import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.repositories
import org.gradle.testing.base.TestingExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class TestKitProjectPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("java-library")
        project.repositories {
            mavenCentral()
        }
        project.plugins.apply("jacoco")
        project.extensions.findByType<TestingExtension>()?.apply {
            suites {
                named<JvmTestSuite>("test") {
                    useJUnitJupiter()
                }
            }
        }
        project.tasks.named<JacocoReport>("jacocoTestReport") {
            reports {
                xml.required.set(true)
            }
        }
        project.tasks.named("test") {
            finalizedBy("jacocoTestReport")
        }
        project.plugins.apply(PublishJvmPublicationPlugin::class.java)
        project.extensions.findByType<JavaPluginExtension>()?.apply {
            withJavadocJar()
            withSourcesJar()
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
}
