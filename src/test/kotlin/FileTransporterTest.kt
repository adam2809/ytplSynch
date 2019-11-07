import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class FileTransporterTest{

    companion object{
        const val DUMMY_FILE_PATH = "/home/adam/code/ytplSynch/src/test/resources/fileTransporterTestDummyFile.txt"
        const val DUMMY_FILE_DEST_PATH = "/sdcard/ytplSynchTest"

        const val INVALID_PATH = "bibibibiBIBIBIBIBIBI"
    }

    private val transporter = FileTransporterFactory.getInstance()

    @Test
    fun transportsFileLinux2Android(){
        transporter.transport(DUMMY_FILE_PATH, DUMMY_FILE_DEST_PATH)

        assertEquals(1,getTestFilesOnDevice().size)
        assertEquals("fileTransporterTestDummyFile.txt",getTestFilesOnDevice()[0])
    }

    @Test
    fun invalidSourcePathThrowsCorrectException(){
        val e = assertThrows<IOException> {
            transporter.transport(INVALID_PATH, DUMMY_FILE_DEST_PATH)
        }
        assertEquals("Source path ($INVALID_PATH) is invalid",e.message)
    }


    @Test
    fun invalidDestPathThrowsCorrectException(){
        val e = assertThrows<IOException> {
            transporter.transport(DUMMY_FILE_PATH,INVALID_PATH)
        }
        assertEquals("Destination path ($INVALID_PATH) is invalid",e.message)
    }

    fun getTestFilesOnDevice():Array<String>{
        return emptyArray()
    }
}