package utils

import java.io.File
import java.io.IOException

fun getTestFilesOnDeviceFromDir(dir:String):List<String>{
    val (output,error) = arrayOf(
        "adb",
        "shell",
        "ls",
        dir
    ).runCommand(File("."))
    return output.split("\n").dropLast(1)
}

fun clearDirOnDevice(dir:String){
    val baseCommand = mutableListOf(
        "adb",
        "shell",
        "rm",
        ""
    )

    val filesOnDevice = getTestFilesOnDeviceFromDir(dir)

    if (filesOnDevice.isEmpty()){
        return
    }

    filesOnDevice.forEach {
        baseCommand[3] = "$dir$it"
        val (_,error) = baseCommand.toTypedArray().runCommand(File("."))
        if (error.isNotEmpty()){
            throw IOException("Could not remove files from $dir on device")
        }
    }
}