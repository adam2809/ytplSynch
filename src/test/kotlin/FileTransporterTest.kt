import filetransport.ADBFileTransportException
import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.clearDirOnDevice
import utils.getFilesOnDeviceFromPath
import utils.runCommand
import java.io.File

class FileTransporterTest{

    companion object{
        const val DUMMY_FILE_PATH = "/home/adam/code/ytplSynch/src/test/resources/fileTransporterTestDummyFile.txt"
        const val DUMMY_FILE_DEST_PATH = "/sdcard/ytplSynchTest/"

        const val INVALID_PATH = "bibibibiBIBIBIBIBIBI"
    }

    @BeforeEach
    private fun setUp(){
        clearDirOnDevice(TestUtils.TestDirs.testDirOnDevice)
    }

    private val transporter = FileTransporterFactory.getInstance()

    @Test
    fun transportsFileLinux2Android(){
        transporter.transport(DUMMY_FILE_PATH, DUMMY_FILE_DEST_PATH)

        val filesInDestDir = getFilesOnDeviceFromPath(TestUtils.TestDirs.testDirOnDevice)
        assertEquals(1,filesInDestDir.size)
        assertEquals("fileTransporterTestDummyFile.txt",filesInDestDir[0])
    }

    @Test
    fun invalidSourcePathThrowsCorrectException(){
        val e = assertThrows<ADBFileTransportException> {
            transporter.transport(INVALID_PATH, DUMMY_FILE_DEST_PATH)
        }
        assertEquals("Source path is invalid",e.msg)
        assertEquals(2,e.errorCode)
        assertEquals(0,getFilesOnDeviceFromPath(TestUtils.TestDirs.testDirOnDevice).size)
    }


    @Test
    fun invalidDestPathThrowsCorrectException(){
        val e = assertThrows<ADBFileTransportException> {
            transporter.transport(DUMMY_FILE_PATH,INVALID_PATH)
        }
        assertEquals("Destination path is invalid",e.msg)
        assertEquals(3,e.errorCode)
        assertEquals(0,getFilesOnDeviceFromPath(TestUtils.TestDirs.testDirOnDevice).size)
    }
}