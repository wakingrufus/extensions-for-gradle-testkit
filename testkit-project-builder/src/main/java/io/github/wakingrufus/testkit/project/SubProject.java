package io.github.wakingrufus.testkit.project;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record SubProject(String name, @Nullable String relativePath) {
}
