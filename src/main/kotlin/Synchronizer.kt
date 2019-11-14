import playliststate.DevicePlaylistState
import playliststate.YTPlaylistState

class Synchronizer(private val sourcePL:String, private val destOnDevice:String, private val tempDir:String){

    fun synchronize(){
        val YTPLState = YTPlaylistState(sourcePL)
    }
    
} 