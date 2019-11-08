package filetransport

class ADBFileTransportException(private val adbOutput: String) : Throwable() {
    var errorCode:Int = -1
    var msg = ""

    init{
        resolveErrorCode()
    }

    private fun resolveErrorCode(){
        with(adbOutput){
            val (m,e) = when{
                contains("no devices/emulators found") -> Pair("No devices connected",1)
                contains("No such file or directory") -> Pair("Source path is invalid",2)
                contains("failed to copy") -> Pair("Destination path is invalid",3)
                else -> Pair("Sth got fucked",4)
            }
            msg = m
            errorCode = e
        }
    }
}