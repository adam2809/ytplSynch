import filedownload.YTAudioFileDownloader
import filetransport.FileTransporterFactory
import playliststate.DevicePlaylistState
import playliststate.YTPlaylistEntry
import playliststate.YTPlaylistState
import java.nio.file.Path
import java.nio.file.Paths

class Synchronizer(sourcePL:String, private val destOnDevice: Path, private val cache:Path){

    private val YTPLState = YTPlaylistState(sourcePL)
    private val devicePLState = DevicePlaylistState(destOnDevice)

    companion object {
        const val YT_DL_FILE_NAME_FORMAT = "%s-%s.m4a"
    }

    fun synchronize(){
        YTPLState.update()
        devicePLState.update()

        removeEntries(devicePLState - YTPLState)
        addEntries(YTPLState - devicePLState)
    }

    private fun addEntries(toAdd:List<YTPlaylistEntry>){
        downloadToCache(toAdd)
        transportCacheToDevice(toAdd)
    }

    private fun downloadToCache(toAdd:List<YTPlaylistEntry>){
        toAdd.map {
            YTAudioFileDownloader(it.ytID,cache)
        }.forEach {
            it.download()
        }
    }

    private fun transportCacheToDevice(toAdd:List<YTPlaylistEntry>) {
        val transporter = FileTransporterFactory.getInstance()
        toAdd.forEach {
            transporter.transport(createPathFromYTPLEntry(cache,it),destOnDevice)
        }
    }

    private fun removeEntries(toRemove:List<YTPlaylistEntry>){
        
    }

    private fun createPathFromYTPLEntry(base:Path,entry:YTPlaylistEntry):Path{
        return Paths.get("$base/${YT_DL_FILE_NAME_FORMAT.format(entry.title,entry.ytID)}")
    }
}