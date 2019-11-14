import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import utils.clearDirOnDevice
import utils.getFilesOnDeviceFromPath


import java.io.File

class PLSynchronizerTest{

    private val TEST_YT_PLAYLIST = "https://www.youtube.com/watch?list=PL1WyaSvUwdxcXb-V08h4rsRLjGCjKoUVd"
    private val TEST_DEST_DIR = "/sdcard/ytplSynchTest/"
    private val TEST_TEMP_DIR = "/home/adam/code/ytplSynch/src/test/resources/SynchronizerTempDir"
    private  val TEST_FILES_DIR = "/home/adam/code/ytplSynch/src/test/resources/PLStatusTestFiles"

    private val syncher = Synchronizer(TEST_YT_PLAYLIST,TEST_DEST_DIR,TEST_TEMP_DIR)

    @BeforeEach
    fun setUp(){
        clearDirOnDevice(TestUtils.TestDirs.testDirOnDevice)
    }

    @Test
    fun testSynchToEmpty(){
        syncher.synchronize()

        assertEquals(3,getFilesOnDeviceFromPath(TestUtils.TestDirs.testDirOnDevice).size)
    }

    @Test
    fun testSynchToPartlySynched(){
        val transporter = FileTransporterFactory.getInstance()
        getFilesInTestDir().slice(0..1).forEach {
//            transporter.transport("$TEST_FILES_DIR/$it", TEST_DEST_DIR)
        }

    }

    @Test
    fun `test in a case when some files are to be removed and some added`(){

    }

    private fun getFilesInTestDir():List<String>{
        return File(TEST_FILES_DIR).walk().toList().drop(1).map {it.name}
    }
}