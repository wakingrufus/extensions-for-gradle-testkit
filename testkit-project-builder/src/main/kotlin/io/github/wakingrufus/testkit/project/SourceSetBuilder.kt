package io.github.wakingrufus.testkit.project

import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files

@TestKitDsl
class SourceSetBuilder internal constructor(private val sourcesDir: File) {
    /**
     * write a java file
     *
     * @param file     the relative path to the java file
     * @param contents the contents of the file
     */
    fun java(file: String, contents: String) {
        language("java", file, contents)
    }

    /**
     * write a java file
     *
     * @param file     the relative path to the java file
     * @param contents the contents of the file. uses a supplier for more ideomatic DSL usage in groovy and kotlin
     */
    fun java(file: String, contents: () -> String) {
        language("java", file, contents())
    }

    /**
     * write a scala file
     *
     * @param file     the relative path to the scala file
     * @param contents the contents of the file
     */
    fun scala(file: String, contents: String) {
        language("scala", file, contents)
    }

    /**
     * write a scala file
     *
     * @param file     the relative path to the scala file
     * @param contents the contents of the file. uses a supplier for more ideomatic DSL usage in groovy and kotlin
     */
    fun scala(file: String, contents: () -> String) {
        language("scala", file, contents())
    }

    /**
     * write a groovy file
     *
     * @param file     the relative path to the groovy file
     * @param contents the contents of the file
     */
    fun groovy(file: String, contents: String) {
        language("groovy", file, contents)
    }

    /**
     * write a groovy file
     *
     * @param file     the relative path to the groovy file
     * @param contents the contents of the file. uses a supplier for more ideomatic DSL usage in groovy and kotlin
     */
    fun groovy(file: String, contents: () -> String) {
        language("groovy", file, contents())
    }

    /**
     * write a kotlin file
     *
     * @param file     the relative path to the kotlin file
     * @param contents the contents of the file
     */
    fun kotlin(file: String, contents: String) {
        language("kotlin", file, contents)
    }

    /**
     * write a kotlin file
     *
     * @param file     the relative path to the kotlin file
     * @param contents the contents of the file. uses a supplier for more ideomatic DSL usage in groovy and kotlin
     */
    fun kotlin(file: String, contents: () -> String) {
        language("kotlin", file, contents())
    }

    /**
     * write a resource file
     *
     * @param file     the relative path to the resource file
     * @param contents the contents of the file
     */
    fun resources(file: String, contents: String) {
        language("resources", file, contents)
    }

    /**
     * write a resource file
     *
     * @param file     the relative path to the resource file
     * @param contents the contents of the file. uses a supplier for more ideomatic DSL usage in groovy and kotlin
     */
    fun resources(file: String, contents: () -> String) {
        language("resources", file, contents())
    }

    /**
     * write a file for specified language
     *
     * @param file     the relative path to the java file
     * @param contents the contents of the file. uses a supplier for more ideomatic DSL usage in groovy and kotlin
     */
    fun language(language: String, file: String, contents: () -> String) {
        language(language, file, contents())
    }

    /**
     * write a file for specified language
     *
     * @param file     the relative path to the file
     * @param contents the contents of the file
     */
    fun language(language: String, file: String, contents: String) {
        val pathToSourceFile = sourcesDir.toPath().resolve(language).resolve(file)
        pathToSourceFile.parent.toFile().mkdirs()
        try {
            Files.write(pathToSourceFile, contents.toByteArray(StandardCharsets.UTF_8))
        } catch (e: IOException) {
            val message = "Error writing java file to $file"
            throw RuntimeException(message, e)
        }
    }
}
