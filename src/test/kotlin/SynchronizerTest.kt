import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import utils.clearDirOnDevice
import utils.getFilesOnDeviceFromPath
import utils.runCommand


import java.io.File
import java.nio.file.Paths

class PLSynchronizerTest{

    private val syncher = Synchronizer(TestUtils.testYTPL,TestUtils.testDirOnDevice,TestUtils.testEmptyPath)

    private val expectedFiles = listOf<String>(
        "Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)-nsufd9Ckiko.m4a",
        "Tiger & Woods - Balloon-oG0XcvGLoq0.m4a",
        "William Onyeabor - Better Change Your Mind (Official)-u7kaOntQbsw.m4a"
    )

    @BeforeEach
    fun setUp(){
        clearDirOnDevice(TestUtils.testDirOnDevice)
        TestUtils.clearPath(TestUtils.testEmptyPath)
    }

    @Test
    fun testSynchToEmpty(){
        syncher.synchronize()

        synchAndAssert()
    }

    @Test
    fun testSynchToPartlySynched(){
        val transporter = FileTransporterFactory.getInstance()
        transporter.transport(Paths.get(TestUtils.testYTPLDownloadedPath.toString()+"/Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)-nsufd9Ckiko.m4a"),TestUtils.testDirOnDevice)

        synchAndAssert()
    }

    @Test
    fun `test in a case when some files are to be removed and some added`(){
        val transporter = FileTransporterFactory.getInstance()
        transporter.transport(Paths.get(TestUtils.testYTPLDownloadedPath.toString()+"/Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)-nsufd9Ckiko.m4a"),TestUtils.testDirOnDevice)
        arrayOf(
            "adb",
            "shell",
            "touch",
            "${TestUtils.testDirOnDevice}/MappyHadness-L0raUb19qwU.m4a"
        ).runCommand(File("."))

        synchAndAssert()
    }

    fun synchAndAssert(){
        syncher.synchronize()

        val filesOnDevice = getFilesOnDeviceFromPath(TestUtils.testDirOnDevice)
        assertEquals(3,filesOnDevice.size)
        assertEquals(expectedFiles,filesOnDevice)
    }
}