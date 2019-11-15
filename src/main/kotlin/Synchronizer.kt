import filedownload.YTAudioFileDownloader
import filetransport.FileTransporterFactory
import playliststate.DevicePlaylistState
import playliststate.YTPlaylistEntry
import playliststate.YTPlaylistState
import utils.deleteFileOnDevice
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

class Synchronizer(sourcePL:String, private val destOnDevice: Path, private val cache:Path){

    private val YTPLState = YTPlaylistState(sourcePL)
    private val devicePLState = DevicePlaylistState(destOnDevice)

    val couldNotDownload = mutableListOf<YTAudioFileDownloader>()
    val couldNotTransport = mutableListOf<YTPlaylistEntry>()
    val couldNotRemove = mutableListOf<YTPlaylistEntry>()

    companion object {
        const val YT_DL_FILE_NAME_FORMAT = "%s-%s.m4a"
    }

    fun synchronize(){
        clearCache()

        YTPLState.update()
        devicePLState.update()

        removeEntries(devicePLState - YTPLState)
        addEntries(YTPLState - devicePLState)

        clearCache()
    }

    private fun addEntries(toAdd:List<YTPlaylistEntry>){
        downloadToCache(toAdd)
        transportCacheToDevice(toAdd)
    }

    private fun downloadToCache(toAdd:List<YTPlaylistEntry>){
        toAdd.map {
            YTAudioFileDownloader(it.ytID,cache)
        }.forEach {
            try {
                it.download()
            }catch(e:Exception){
                println(e.message)
                couldNotDownload.add(it)
            }
        }
    }

    private fun transportCacheToDevice(toAdd:List<YTPlaylistEntry>) {
        val transporter = FileTransporterFactory.getInstance()
        toAdd.forEach {
            try{
                transporter.transport(createPathFromYTPLEntry(cache,it),destOnDevice)
            }catch (e:Exception){
                println(e.message)
                couldNotTransport.add(it)
            }
        }
    }

    private fun removeEntries(toRemove:List<YTPlaylistEntry>){
        toRemove.forEach {
            try {
                deleteFileOnDevice(createPathFromYTPLEntry(destOnDevice,it))
            }catch (e:Exception){
                println(e.message)
                couldNotRemove.add(it)
            }
        }
    }

    private fun createPathFromYTPLEntry(base:Path,entry:YTPlaylistEntry):Path{
        return Paths.get("$base/${YT_DL_FILE_NAME_FORMAT.format(entry.title,entry.ytID)}")
    }

    fun clearCache(){
        Files.walk(cache).toList().drop(1).forEach {
            Files.delete(it)
        }
    }
}