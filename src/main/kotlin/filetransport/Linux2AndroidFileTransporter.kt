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
        ).runCommand(File(".")).also { println(it) }

        if(!output.contains("error")){
            return
        }
        var msg:String
        with(output){
            msg = when{
                contains("no devices/emulators found") -> "No devices connected"
                else -> "Sth got fucked"
            }
        }
        throw IOException(msg)
    }
}