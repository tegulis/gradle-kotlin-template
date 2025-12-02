package net.tegulis.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction

open class ReplaceVersion : DefaultTask() {
    @get:Internal val projectVersion: String = project.version.toString()

    @get:Internal val projectExtensions: ExtensionContainer = project.extensions

    init {
        group = "other"
        description = "Replaces the VERSION constant in the main class file(s) with the project version."
    }

    @TaskAction
    fun processFiles() {
        val sourceSets =
            projectExtensions.findByName("sourceSets") as? SourceSetContainer
                ?: throw IllegalArgumentException("Java plugin is not applied.")
        sourceSets.forEachFile { file ->
            if (file.name !in listOf("Main.kt", "Library.kt")) return@forEachFile
            logger.lifecycle("processing ${file.path}")
            val contents = file.readText()
            val modifiedContents = replaceVersion(contents, projectVersion)
            file.writeText(modifiedContents)
        }
    }
}

fun replaceVersion(input: String, newVersion: String): String {
    val searchRegex = Regex("(\\sVERSION\\s*=.*)\"(.*?)\"")
    return input.replace(searchRegex, "$1\"${newVersion}\"")
}
