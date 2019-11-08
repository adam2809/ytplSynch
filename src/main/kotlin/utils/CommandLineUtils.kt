package utils

import java.io.File
import java.util.concurrent.TimeUnit

fun Array<String>.runCommand(workingDir: File): Pair<String,String> {
    val proc = ProcessBuilder(*this)
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    proc.waitFor(60, TimeUnit.MINUTES)
    return Pair(proc.inputStream.bufferedReader().readText(), proc.errorStream.bufferedReader().readText())
}
