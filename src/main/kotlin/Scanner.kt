package com.bojanludajic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Path

interface Occurrence {
    val file: Path
    val line: Int
    val offset: Int
}

fun searchForTextOccurrences(
    stringToSearch: String,
    directory: Path
): Flow<Occurrence> = flow {
    val file = directory.toFile()
    if(file.isDirectory) {
        searchDirectory(file, stringToSearch) { occurrence -> emit(occurrence) }
    } else {
        searchFile(file, stringToSearch) { occurrence -> emit(occurrence) }
    }
}

suspend fun searchDirectory(
    dir: File,
    stringToSearch: String,
    emitOccurrence: suspend (Occurrence) -> Unit
) {
    dir.listFiles()?.forEach { file ->
        if(file.isDirectory) {
            searchDirectory(file, stringToSearch, emitOccurrence)
        } else {
            searchFile(file, stringToSearch, emitOccurrence)
        }
    }
}

suspend fun searchFile(
    file: File,
    stringToSearch: String,
    emitOccurrence: suspend (Occurrence) -> Unit
) {
    val br = BufferedReader(FileReader(file))
    var line = br.readLine()
    var lineNum = 1
    while(line != null) {
        var offset = line.indexOf(stringToSearch)
        while(offset >= 0) {
            emitOccurrence(object: Occurrence {
                override val file = file.toPath()
                override val line = lineNum
                override val offset = offset
            })
            offset = line.indexOf(stringToSearch, offset + 1)
        }
        lineNum++
        line = br.readLine()
    }
}