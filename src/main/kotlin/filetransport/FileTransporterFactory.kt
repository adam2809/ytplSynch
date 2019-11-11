package filetransport

import java.lang.Exception

object FileTransporterFactory{
    fun getInstance():FileTransporter{
        if(System.getProperty("os.name") == "Linux"){
            return Linux2AndroidFileTransporter
        }
        throw Exception("Your operating system was not recognised")
    }
}