package io.github.wakingrufus.testkit.project

import org.jspecify.annotations.NullMarked

sealed class Repository

@NullMarked
internal class BuiltIn(val functionName: String) : Repository()

@NullMarked
internal class Maven(val url: String) : Repository()