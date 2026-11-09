plugins {
    base
    id("jacoco-report-aggregation")
    id("org.jreleaser") version ("1.26.0")
}
tasks.wrapper {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "9.7.0"
    distributionSha256Sum = "84fbba45c7f4c64abc77460e1c00f541e9f960e3c7ed2538f1ede19eacd873ae"
}
repositories {
    mavenCentral() // needed for jacoco
}
dependencies {
    subprojects.forEach {
        jacocoAggregation(project(":" + it.name))
    }
}
reporting {
    reports {
        register("javaCodeCoverageReport", JacocoCoverageReport::class.java) {
            testSuiteName = "test"
        }
    }
}
tasks.named("check"){
    finalizedBy(tasks.named("javaCodeCoverageReport"))
}
jreleaser {
    signing {
        armored = true
        publicKey = System.getenv("PUBLIC_KEY")
        secretKey = System.getenv("PRIVATE_KEY")
        passphrase = System.getenv("PASSPHRASE")
    }
    release {
        github {
            repoOwner = "wakingrufus"
            host = "github.com"
            username = "wakingrufus"
            apiEndpoint = "https://api.github.com"
            token = System.getenv("GITHUB_TOKEN")
            enabled = true
            releaseName = "{{tagName}}"
            releaseNotes {
                enabled = true
            }
            changelog {
                enabled = false
            }
            skipTag = true
            sign = false
        }
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                    applyMavenCentralRules = true
                    username = System.getenv("SONATYPE_USER")
                    password = System.getenv("SONATYPE_PASS")
                    namespace.set("io.github.wakingrufus")
                    retryDelay = 60
                    maxRetries = 100
                    artifactOverride {
                        artifactId = "khtmx-dsl-js"
                        jar = false
                        verifyPom = false
                        sourceJar = false
                        javadocJar = false
                    }
                }
            }
        }
    }
}
project.tasks.named("jreleaserFullRelease") {
    dependsOn(subprojects.map { it.tasks.named("publish") })
}