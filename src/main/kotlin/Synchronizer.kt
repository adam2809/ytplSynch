import playliststate.DevicePlaylistState
import playliststate.YTPlaylistState
import java.nio.file.Path

class Synchronizer(private val sourcePL:String, private val destOnDevice: Path, private val tempDir:Path){

    fun synchronize(){
        val YTPLState = YTPlaylistState(sourcePL)
    }
    
} 