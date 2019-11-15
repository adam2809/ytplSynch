import filedownload.YTAudioFileDownloader
import filetransport.FileTransporterFactory
import playliststate.PlaylistState
import playliststate.YTPlaylistEntry
import utils.deleteFileOnDevice
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

class Synchronizer(private val sourceState: PlaylistState, private val destState: PlaylistState,
                   private val destOnDevice:Path, private val cache:Path){

    companion object {
        const val YT_DL_FILE_NAME_FORMAT = "%s-%s.m4a"
    }

    fun synchronize(){
        clearCache()

        sourceState.update()
        destState.update()

        removeEntries(destState - sourceState)
        addEntries(sourceState - destState)

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
        toRemove.forEach {
            deleteFileOnDevice(createPathFromYTPLEntry(destOnDevice,it))
        }
    }

    private fun createPathFromYTPLEntry(base:Path,entry:YTPlaylistEntry):Path{
        return Paths.get("$base/${YT_DL_FILE_NAME_FORMAT.format(entry.title,entry.ytID)}")
    }

    private fun clearCache(){
        Files.walk(cache).toList().drop(1).forEach {
            Files.delete(it)
        }
    }
}