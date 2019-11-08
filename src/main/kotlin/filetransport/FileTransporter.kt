package filetransport

interface FileTransporter {
    fun transport(source:String,dest:String)
}