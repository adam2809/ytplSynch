package utils

import java.io.File
import java.io.IOException
import java.nio.file.Path

fun getFilesOnDeviceFromPath(path: Path):List<String>{
    val (output,error) = arrayOf(
        "adb",
        "shell",
        "ls",
        path.toString()
    ).runCommand(File("."))

    if(error.isNotBlank()){
        throw IOException("Could not run ls on $path the error was \n$error")
    }

    return output.split("\n").dropLast(1)
}

fun clearDirOnDevice(path:Path){
    val baseCommand = mutableListOf(
        "adb",
        "shell",
        "rm",
        ""
    )

    val filesOnDevice = getFilesOnDeviceFromPath(path)

    if (filesOnDevice.isEmpty()){
        return
    }

    filesOnDevice.forEach {
        baseCommand[3] = "${path}/'$it'"
        val (_,error) = baseCommand.toTypedArray().runCommand(File("."))
        if (error.isNotEmpty()){
            throw IOException("Could not remove files from $path on device")
        }
    }
}