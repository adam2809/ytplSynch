import filetransport.FileTransporterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import playliststate.DevicePlaylistState
import playliststate.PlaylistEntry
import playliststate.YTPlaylistEntry
import playliststate.YTPlaylistState
import utils.clearDirOnDevice
import java.io.File

class PlaylistStateTest{

    companion object{
        const val TEST_YT_PLAYLIST = "https://www.youtube.com/watch?list=PL1WyaSvUwdxcXb-V08h4rsRLjGCjKoUVd"
        const val TEST_FILES_DIR = "/home/adam/code/ytplSynch/src/test/resources/PLStatusTestFiles"
        const val TEST_FILES_DEST = "/sdcard/ytplSynchTest"
    }


    private val expectedEntries = listOf<PlaylistEntry>(
        YTPlaylistEntry("nsufd9Ckiko","Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"),
        YTPlaylistEntry("oG0XcvGLoq0","Tiger & Woods - Balloon"),
        YTPlaylistEntry("u7kaOntQbsw","William Onyeabor - Better Change Your Mind (Official)")
    )

    @Test
    fun testYTImplementation(){
        val state = YTPlaylistState(TEST_YT_PLAYLIST)
        assertEquals(expectedEntries,state.entries)
    }
    @Test
    fun testDeviceImplementation(){
        val state = DevicePlaylistState(TEST_FILES_DEST)

        assertEquals(expectedEntries,state.entries)
    }
}