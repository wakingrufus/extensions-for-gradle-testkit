plugins {
    base
    id("jacoco-report-aggregation")
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
        val javaCodeCoverageReport by creating(JacocoCoverageReport::class) {
            testSuiteName = "test"
        }
    }
}
tasks.named("check"){
    finalizedBy(tasks.named("javaCodeCoverageReport"))
}