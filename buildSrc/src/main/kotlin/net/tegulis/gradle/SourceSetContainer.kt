package net.tegulis.gradle

import java.io.File
import org.gradle.api.tasks.SourceSetContainer

fun SourceSetContainer.forEachFile(block: (File) -> Unit) {
    for (sourceSet in this) {
        for (srcDir in sourceSet.allSource.srcDirs) {
            val files = srcDir.walkTopDown().filter { file -> file.isFile }
            for (file in files) {
                block(file)
            }
        }
    }
}
