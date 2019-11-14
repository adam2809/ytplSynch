package filetransport

import utils.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.nio.file.Path

object Linux2AndroidFileTransporter:FileTransporter{
    override fun transport(source: Path, dest: Path) {
        val (output,_) = arrayOf(
            "adb",
            "push",
            source.toString(),
            dest.toString()
        ).runCommand(File("."))

        if(output.contains("error")){
            throw ADBFileTransportException(output)
        }
    }
}