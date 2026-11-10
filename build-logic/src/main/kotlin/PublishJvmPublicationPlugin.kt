import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.*

fun Project.publishToLocalStaging(name: String = "staging-deploy") {
    plugins.withId("maven-publish") {
        extensions.findByType<PublishingExtension>()?.apply {
            repositories.maven(project.rootProject.layout.buildDirectory.dir(name).get().asFile.toURI())
        }
    }
}

class PublishJvmPublicationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("maven-publish")
        project.publishToLocalStaging()
        project.extensions.findByType<PublishingExtension>()?.apply {
            publications {
                create<MavenPublication>("maven") {
                    groupId = project.group.toString()
                    artifactId = project.name

                    from(project.components["java"])

                    pom {
                        name = project.name
                        description = project.description
                        url = "https://github.com/wakingrufus/extensions-for-gradle-testkit"
                        licenses {
                            license {
                                name = "Apache-2.0"
                                url = "https://spdx.org/licenses/Apache-2.0.html"
                            }
                        }
                        developers {
                            developer {
                                id = "wakingrufus"
                                name = "John Burns"
                            }
                        }
                        scm {
                            connection = "scm:git:https://github.com/wakingrufus/extensions-for-gradle-testkit.git"
                            developerConnection = "scm:git:ssh://github.com/wakingrufus/extensions-for-gradle-testkit.git"
                            url = "http://github.com/wakingrufus/extensions-for-gradle-testkit"
                        }
                    }
                }
            }
        }
    }
}
