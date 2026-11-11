# Extensions for Gradle TestKit


## testkit-runner
[![Maven Central](https://img.shields.io/maven-central/v/io.github.wakingrufus/testkit-runner?style=for-the-badge&color=01AF01)](https://repo1.maven.org/maven2/io/github/wakingrufus/testkit-runner/)

The `TestProjectRunner` allows you to invoke the Gradle TestKit runner, with options, on your project. This is just a very thin wrapper around the TestKit GradleRunner that provides more ideomatic syntax for Groovy and Kotlin and handles setting the correct project directory for you. All options of the TestKit GradleRunner are exposed through the invocation DSL. The result is a TestKit `BuildResult`.

Example:
```kotlin
val result = runner.run("build") {
    forwardOutput()
    withGradleVersion("9.0.0")
}
```
#### Testing against multiple versions of Gradle
The class `io.github.wakingrufus.testkit.runner.Gradle` is useful for defining which versions of Gradle to test against. For example, you can define an enum such as:
```java
public enum SupportedGradleVersion {
MIN(Gradle.ofVersion("9.1.0")), CURRENT(Gradle.current());

    public final Gradle version;

    SupportedGradleVersion(Gradle version) {
        this.version = version;
    }
}
```
`Gradle.current()` means the Gradle distribution of your project will be used. Then, you can parameterize your tests with JUnit5/Jupiter:
```kotlin
@ParameterizedTest
@EnumSource(SupportedGradleVersion::class)
fun `test against supported gradle versions`(gradleVersion: SupportedGradleVersion) {
    val runner = testProject(testProjectDir) {
        // setup project
    }
    val result = runner.run("build") {
        withGradle(gradleVersion.version)
    }
}
```

### testkit-assertions
[![Maven Central](https://img.shields.io/maven-central/v/io.github.wakingrufus/testkit-assertions?style=for-the-badge&color=01AF01)](https://repo1.maven.org/maven2/io/github/wakingrufus/testkit-assertions/)

This library also provides custom AssertJ assertions for the BuildResult. These can be used in Java, Kotlin, or Groovy, as long as you are using AssertJ.

Example:
```kotlin
assertThat(result)
    .hasNoDeprecationWarnings()
    .hasNoMutableStateWarnings()
assertThat(result).task(":compileJava").hasOutcome(TaskOutcome.SUCCESS)
assertThat(result).task(":build").hasOutcome(TaskOutcome.SUCCESS)
```
