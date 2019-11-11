package utils

import java.io.File

fun getTestFilesOnDeviceFromDir(dir:String):List<String>{
    val (output,error) = arrayOf(
        "adb",
        "shell",
        "ls",
        dir
    ).runCommand(File("."))
    return output.split("\n").dropLast(1)
}