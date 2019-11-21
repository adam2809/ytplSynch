import filetransport.ADBFileTransportException
import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.TestUtils
import utils.clearDirOnDevice
import utils.getFilesOnDeviceFromPath
import java.nio.file.Paths

class FileTransporterTest{

    companion object{
        val DUMMY_FILE_PATH = Paths.get("/home/adam/code/ytplSynch/src/test/resources/fileTransporterTestDummyFile.txt")
        val INVALID_PATH = Paths.get("bibibibiBIBIBIBIBIBI")
    }

    @BeforeEach
    private fun setUp(){
        clearDirOnDevice(TestUtils.testDirOnDevice)
    }

    private val transporter = FileTransporterFactory.getInstance()

    @Test
    fun transportsFileLinux2Android(){
        transporter.transport(DUMMY_FILE_PATH, TestUtils.testDirOnDevice)

        val filesInDestDir = getFilesOnDeviceFromPath(TestUtils.testDirOnDevice)
        assertEquals(1,filesInDestDir.size)
        assertEquals("fileTransporterTestDummyFile.txt",filesInDestDir[0])
    }

    @Test
    fun invalidSourcePathThrowsCorrectException(){
        val e = assertThrows<ADBFileTransportException> {
            transporter.transport(INVALID_PATH, TestUtils.testDirOnDevice)
        }
        assertEquals("Source path is invalid",e.msg)
        assertEquals(2,e.errorCode)
        assertEquals(0,getFilesOnDeviceFromPath(TestUtils.testDirOnDevice).size)
    }


    @Test
    fun invalidDestPathThrowsCorrectException(){
        val e = assertThrows<ADBFileTransportException> {
            transporter.transport(DUMMY_FILE_PATH,INVALID_PATH)
        }
        assertEquals("Destination path is invalid",e.msg)
        assertEquals(3,e.errorCode)
        assertEquals(0,getFilesOnDeviceFromPath(TestUtils.testDirOnDevice).size)
    }
}