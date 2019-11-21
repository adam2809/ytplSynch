package utils

import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths

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
    val filesOnDevice = getFilesOnDeviceFromPath(path)

    if (filesOnDevice.isEmpty()){
        return
    }

    filesOnDevice.forEach {
        deleteFileOnDevice(Paths.get("$path/'$it'"))
    }
}

fun deleteFileOnDevice(path:Path){
    val baseCommand = arrayOf(
        "adb",
        "shell",
        "rm",
        "$path"
    )

    val (_,error) = baseCommand.runCommand(File("."))
    if (error.isNotEmpty()){
        throw IOException("Could not remove files from $path on device. ADB error was:\n$error")
    }
}