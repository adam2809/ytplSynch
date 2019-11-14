import playliststate.DevicePlaylistState
import playliststate.YTPlaylistState
import java.nio.file.Path

class Synchronizer(sourcePL:String, destOnDevice: Path, private val tempDir:Path){

    private val YTPLState = YTPlaylistState(sourcePL)
    private val devicePLState = DevicePlaylistState(destOnDevice)

    fun synchronize(){
        YTPLState.update()
        devicePLState.update()


    }
    
} 