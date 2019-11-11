import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import playliststate.PlaylistEntry
import playliststate.YTPlaylistEntry
import playliststate.YTPlaylistState

class PlaylistStateTest{

    companion object{
        const val TEST_YT_PLAYLIST = "https://www.youtube.com/watch?list=PL1WyaSvUwdxcXb-V08h4rsRLjGCjKoUVd"
    }

    @Test
    fun testYTImplementation(){
        val state = YTPlaylistState(TEST_YT_PLAYLIST)
        val expectedEntries = listOf<PlaylistEntry>(
            YTPlaylistEntry("nsufd9Ckiko","Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"),
            YTPlaylistEntry("oG0XcvGLoq0","Tiger & Woods - Balloon"),
            YTPlaylistEntry("u7kaOntQbsw","William Onyeabor - Better Change Your Mind (Official)")
        )
        assertEquals(expectedEntries,state.entries)
    }
    @Test
    fun testDeviceImplementation(){

    }
}