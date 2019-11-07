import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

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

        if (testFilesOnDevice.size == 1){
            return
        }

        testFilesOnDevice.forEach {
            baseCommand[3] = "$DUMMY_FILE_DEST_PATH$it"
            val (output,error) = baseCommand.toTypedArray().runCommand(File("."))
            if (error.isNotEmpty()){
                fail<Nothing>("Test directory cleanup failed")
            }
        }
    }

    private val transporter = FileTransporterFactory.getInstance()

    @Test
    fun transportsFileLinux2Android(){
        transporter.transport(DUMMY_FILE_PATH, DUMMY_FILE_DEST_PATH)

        assertEquals(2,getTestFilesOnDevice().size)
        assertEquals("fileTransporterTestDummyFile.txt",getTestFilesOnDevice()[1])
    }

    @Test
    fun invalidSourcePathThrowsCorrectException(){
        val e = assertThrows<IOException> {
            transporter.transport(INVALID_PATH, DUMMY_FILE_DEST_PATH)
        }
        assertEquals("Source path ($INVALID_PATH) is invalid",e.message)
        assertEquals(1,getTestFilesOnDevice().size)
    }


    @Test
    fun invalidDestPathThrowsCorrectException(){
        val e = assertThrows<IOException> {
            transporter.transport(DUMMY_FILE_PATH,INVALID_PATH)
        }
        assertEquals("Destination path ($INVALID_PATH) is invalid",e.message)
        assertEquals(1,getTestFilesOnDevice().size)
    }

    private fun getTestFilesOnDevice():List<String>{
        val (output,error) = arrayOf(
            "adb",
            "shell",
            "ls",
            DUMMY_FILE_DEST_PATH
        ).runCommand(File("."))
        return output.split("\n")
    }

    private fun Array<String>.runCommand(workingDir: File): Pair<String,String> {
        val proc = ProcessBuilder(*this)
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return Pair(proc.inputStream.bufferedReader().readText(), proc.errorStream.bufferedReader().readText())
    }
}