package filetransport

import utils.*
import java.io.File
import java.io.IOException
import java.lang.Exception

class Linux2AndroidFileTransporter:FileTransporter{
    override fun transport(source: String, dest: String) {
        val (output,_) = arrayOf(
            "adb",
            "push",
            source,
            dest
        ).runCommand(File("."))

        if(!output.contains("error")){
            return
        }
        throw ADBFileTransportException(output)
    }
}