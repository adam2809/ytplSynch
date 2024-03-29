import filedownload.FileDownloader
import filedownload.FileDownloaderFactory
import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import playliststate.DevicePlaylistState
import playliststate.PlaylistState
import playliststate.YTPlaylistEntry
import utils.*


import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

class PLSynchronizerTest{

    private val testYTPLState = object:PlaylistState(){
        override var entries: List<YTPlaylistEntry> = listOf(
            YTPlaylistEntry("nsufd9Ckiko","Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"),
            YTPlaylistEntry("oG0XcvGLoq0","Tiger & Woods - Balloon"),
            YTPlaylistEntry("u7kaOntQbsw","William Onyeabor - Better Change Your Mind (Official)")
        )

        override fun update() {
        }
    }

    private val testYTPLStateWithInvalid = object:PlaylistState(){
        override var entries: List<YTPlaylistEntry> = listOf(
            YTPlaylistEntry("nsufd9Ckiko","Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"),
            YTPlaylistEntry("oG0XcvGLoq0","Tiger & Woods - Balloon"),
            YTPlaylistEntry("oG0XcvGLoq0pira","This video does not exist"),
            YTPlaylistEntry("u7kaOntQbsw","William Onyeabor - Better Change Your Mind (Official)")
        )

        override fun update() {
        }
    }

    private val testDownloaderFactory = TestFileDownloaderFactory()

    private val syncher = Synchronizer(testYTPLState, DevicePlaylistState(TestUtils.testDirOnDevice),
        testDownloaderFactory, TestUtils.testDirOnDevice, TestUtils.testEmptyPath)

    private val expectedFiles = listOf(
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
        synchAndAssert()
    }

    @Test
    fun testSynchToPartlySynched(){
        val transporter = FileTransporterFactory.getInstance()
        transporter.transport(Paths.get(TestUtils.testYTPLDownloadedPath.toString()+"/Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)-nsufd9Ckiko.m4a"),
            TestUtils.testDirOnDevice)

        synchAndAssert()
    }

    @Test
    fun `test in a case when some files are to be removed and some added`(){
        val transporter = FileTransporterFactory.getInstance()
        transporter.transport(Paths.get(TestUtils.testYTPLDownloadedPath.toString()+"/Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)-nsufd9Ckiko.m4a"),
            TestUtils.testDirOnDevice)
        arrayOf(
            "adb",
            "shell",
            "touch",
            "${TestUtils.testDirOnDevice}/'Mappy Hadness-L0raUb19qwU.m4a'"
        ).runCommand(File("."))

        synchAndAssert()
    }

    @Test
    fun testDownloadErrorHanding(){
        val syncherThatWillFail = Synchronizer(testYTPLStateWithInvalid, DevicePlaylistState(TestUtils.testDirOnDevice),
                                               testDownloaderFactory, TestUtils.testDirOnDevice, TestUtils.testEmptyPath)

        syncherThatWillFail.synchronize()

        val filesOnDevice = getFilesOnDeviceFromPath(TestUtils.testDirOnDevice)
        assertEquals(3,filesOnDevice.size)
        assertEquals(expectedFiles,filesOnDevice)
        assertEquals(listOf(testYTPLStateWithInvalid.entries[2]),syncherThatWillFail.couldNotBeDownloaded)
    }

    @Test
    fun testDoesntDownloadIfInCache(){

    }

    fun synchAndAssert(){
        syncher.synchronize()

        val filesOnDevice = getFilesOnDeviceFromPath(TestUtils.testDirOnDevice)
        assertEquals(3,filesOnDevice.size)
        assertEquals(expectedFiles,filesOnDevice)
    }
}