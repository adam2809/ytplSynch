package filetransport

class FileTransporterFactory{
    companion object{
        fun getInstance():FileTransporter{
            return Linux2AndroidFileTransporter()
        }
    }
}