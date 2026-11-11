package io.github.wakingrufus.testkit.runner

import org.gradle.api.logging.configuration.WarningMode
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.util.Locale.getDefault

class TestProjectRunner(private val projectDir: File) {
    /**
     * runs given tasks, expecting a successful build
     *
     * @param customizations customizations for the runner to apply after the defaults are set, but before execution
     * @param args           tasks to run
     * @return the BuildResult
     */
    fun run(
        args: List<String>,
        warningMode: WarningMode = WarningMode.All,
        withPluginClasspath: Boolean = true,
        customizations: GradleRunner.() -> Unit = {}
    ): BuildResult {
        val fullArgsList: MutableList<String> = ArrayList(args)
        fullArgsList.add("--warning-mode=" + warningMode.name.lowercase(getDefault()))
        val runner = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments(fullArgsList)
        if (withPluginClasspath) {
            runner.withPluginClasspath()
        }
        customizations.invoke(runner)
        return runner.build()
    }

    /**
     * runs given tasks, expecting a failed build
     *
     * @param customizations customizations for the runner to apply after the defaults are set, but before execution
     * @param args           tasks to run
     * @return the BuildResult
     */
    fun runAndFail(
        args: List<String>,
        warningMode: WarningMode = WarningMode.All,
        withPluginClasspath: Boolean = true,
        customizations: GradleRunner.() -> Unit = {}
    ): BuildResult {
        val fullArgsList: MutableList<String> = ArrayList(args)
        fullArgsList.add("--warning-mode=" + warningMode.name.lowercase(getDefault()))
        val runner = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments(fullArgsList)
        if (withPluginClasspath) {
            runner.withPluginClasspath()
        }
        customizations.invoke(runner)
        return runner.buildAndFail()
    }

    /**
     * Run a build with expectation of success.
     * This method will throw an exception if the build fails.
     */
    fun run(
        vararg args: String,
        warningMode: WarningMode = WarningMode.All,
        withPluginClasspath: Boolean = true,
        customizer: GradleRunner.() -> Unit = {}
    ): BuildResult {
        return run(args.asList(), warningMode, withPluginClasspath, customizer)
    }

    /**
     * Run a build with expectation of failure.
     * This method will throw an exception if the build succeeds.
     */
    fun runAndFail(
        vararg args: String,
        warningMode: WarningMode = WarningMode.All,
        withPluginClasspath: Boolean = true,
        customizer: GradleRunner.() -> Unit = {}
    ): BuildResult {
        return runAndFail(args.asList(), warningMode, withPluginClasspath, customizer)
    }
}
