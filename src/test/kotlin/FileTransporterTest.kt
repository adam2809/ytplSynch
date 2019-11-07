import filetransport.FileTransporter
import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Test

class FileTransporterTest{

    companion object{
        const val DUMMY_FILE_PATH = "/home/adam/code/ytplSynch/src/test/resources/fileTransporterTestDummyFile.txt"
        const val DUMMY_FILE_DEST_PATH = "/home/adam/code/ytplSynch/src/test/resources/fileTransporterTestDummyFile.txt"
    }

    @Test
    fun transportsFileLinux2Android(){
        val trasnporter:FileTransporter = FileTransporterFactory.getInstance()
    }
}