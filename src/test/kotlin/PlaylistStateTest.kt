import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import playliststate.DevicePlaylistState
import playliststate.PlaylistState
import playliststate.YTPlaylistEntry
import playliststate.YTPlaylistState
import utils.TestUtils

class PlaylistStateTest{
    private val expectedEntries = listOf(
        YTPlaylistEntry("nsufd9Ckiko","Alexander Robotnick - Undicidisco (Justin VanDerVolgen Edit)"),
        YTPlaylistEntry("oG0XcvGLoq0","Tiger & Woods - Balloon"),
        YTPlaylistEntry("u7kaOntQbsw","William Onyeabor - Better Change Your Mind (Official)")
    )

    @Test
    fun testYTImplementation(){
        val state = YTPlaylistState(TestUtils.testYTPL)
        state.update()
        assertEquals(expectedEntries,state.entries)
    }
    @Test
    fun testDeviceImplementation(){
        val state = DevicePlaylistState(TestUtils.testYTPLTransportedPath)
        state.update()
        assertEquals(expectedEntries,state.entries)
    }

    @Test
    fun testMinusOperatorOverload1(){
        val state1 = object: PlaylistState() {
            override var entries: List<YTPlaylistEntry> = emptyList()
                get() = expectedEntries

            override fun update() {
                return
            }

        }

        val state2 = object: PlaylistState() {
            override var entries: List<YTPlaylistEntry> = emptyList()
                get() = expectedEntries.slice(0..1)

            override fun update() {
                return
            }

        }
        assertEquals(listOf(expectedEntries[2]),state1 - state2)
    }

    @Test
    fun testMinusOperatorOverload2(){
        val state1 = object: PlaylistState() {
            override var entries: List<YTPlaylistEntry> = emptyList()
                get() = expectedEntries.slice(0..1)

            override fun update() {
                return
            }

        }

        val state2 = object: PlaylistState() {
            override var entries: List<YTPlaylistEntry> = emptyList()
                get() = expectedEntries

            override fun update() {
                return
            }

        }
        assertEquals(emptyList<YTPlaylistEntry>(),state1 - state2)
    }

    @Test
    fun testMinusOperatorOverload3(){
        val state1 = object: PlaylistState() {
            override var entries: List<YTPlaylistEntry> = emptyList()
                get() = listOf(expectedEntries[0],expectedEntries[2])

            override fun update() {
                return
            }

        }

        val state2 = object: PlaylistState() {
            override var entries: List<YTPlaylistEntry> = emptyList()
                get() = listOf(expectedEntries[1])

            override fun update() {
                return
            }

        }
        assertEquals(listOf(expectedEntries[0],expectedEntries[2]),state1 - state2)
    }
}