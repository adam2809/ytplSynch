import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import playliststate.DevicePlaylistState
import playliststate.PlaylistEntry
import playliststate.YTPlaylistEntry
import playliststate.YTPlaylistState

class PlaylistStateTest{
    private val expectedEntries = listOf<PlaylistEntry>(
        YTPlaylistEntry("nsufd9Ckiko","Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"),
        YTPlaylistEntry("oG0XcvGLoq0","Tiger & Woods - Balloon"),
        YTPlaylistEntry("u7kaOntQbsw","William Onyeabor - Better Change Your Mind (Official)")
    )

    @Test
    fun testYTImplementation(){
        val state = YTPlaylistState(TestUtils.testYTPL)
        assertEquals(expectedEntries,state.entries)
    }
    @Test
    fun testDeviceImplementation(){
        val state = DevicePlaylistState(TestUtils.testYTPLTransportedPath)

        assertEquals(expectedEntries,state.entries)
    }
}