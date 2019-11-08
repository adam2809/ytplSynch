import filetransport.ADBFileTransportException
import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import utils.runCommand
import java.io.File

class FileTransporterTest{

    companion object{
        const val DUMMY_FILE_PATH = "/home/adam/code/ytplSynch/src/test/resources/fileTransporterTestDummyFile.txt"
        const val DUMMY_FILE_DEST_PATH = "/sdcard/ytplSynchTest/"

        const val INVALID_PATH = "bibibibiBIBIBIBIBIBI"
    }

    @BeforeEach
    private fun clearTestDirOnDevice(){
        val baseCommand = mutableListOf(
            "adb",
            "shell",
            "rm",
            "/sdcard/ytplSynchTest/"
        )

        val testFilesOnDevice = getTestFilesOnDevice()

        if (testFilesOnDevice.isEmpty()){
            return
        }

        testFilesOnDevice.forEach {
            baseCommand[3] = "$DUMMY_FILE_DEST_PATH$it"
            val (output,error) = baseCommand.toTypedArray().runCommand(File("."))
            if (error.isNotEmpty()){
                fail<Nothing>("Test directory cleanup failed\nThe error was:\n$error")
            }
        }
    }

    private val transporter = FileTransporterFactory.getInstance()

    @Test
    fun transportsFileLinux2Android(){
        transporter.transport(DUMMY_FILE_PATH, DUMMY_FILE_DEST_PATH)

        val temp = getTestFilesOnDevice()
        assertEquals(1,temp.size)
        assertEquals("fileTransporterTestDummyFile.txt",getTestFilesOnDevice()[0])
    }

    @Test
    fun invalidSourcePathThrowsCorrectException(){
        val e = assertThrows<ADBFileTransportException> {
            transporter.transport(INVALID_PATH, DUMMY_FILE_DEST_PATH)
        }
        assertEquals("Source path is invalid",e.msg)
        assertEquals(2,e.errorCode)
        assertEquals(0,getTestFilesOnDevice().size)
    }


    @Test
    fun invalidDestPathThrowsCorrectException(){
        val e = assertThrows<ADBFileTransportException> {
            transporter.transport(DUMMY_FILE_PATH,INVALID_PATH)
        }
        assertEquals("Destination path is invalid",e.msg)
        assertEquals(3,e.errorCode)
        assertEquals(0,getTestFilesOnDevice().size)
    }

    private fun getTestFilesOnDevice():List<String>{
        val (output,error) = arrayOf(
            "adb",
            "shell",
            "ls",
            DUMMY_FILE_DEST_PATH
        ).runCommand(File("."))
        return output.split("\n").dropLast(1)
    }
}