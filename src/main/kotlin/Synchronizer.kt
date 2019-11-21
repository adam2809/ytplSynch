import filedownload.FileDownloaderFactory
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
                   private val downloaderFactory:FileDownloaderFactory, private val destOnDevice:Path, private val cache:Path){

    var couldNotBeDownloaded = mutableListOf<YTPlaylistEntry>()
    var couldNotBeTransported = mutableListOf<YTPlaylistEntry>()
    var couldNotBeRemoved = mutableListOf<YTPlaylistEntry>()

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
        println("Starting to add files. Entries to add are:\n$toAdd")
        downloadToCache(toAdd)
        transportCacheToDevice(toAdd)
    }

    private fun downloadToCache(toAdd:List<YTPlaylistEntry>){
        toAdd.map {
            downloaderFactory.getInstance(it.ytID,cache)
        }.forEachIndexed { i,downloader ->
            try {
                downloader.download()
            }catch (e:Exception){
                couldNotBeDownloaded.add(toAdd[i])
                println("Could not download ${toAdd[i]}. The exception was:\n$e")
            }finally {
                println("$i of ${toAdd.size} files downloaded")
            }
        }
    }

    private fun transportCacheToDevice(toAdd:List<YTPlaylistEntry>) {
        val transporter = FileTransporterFactory.getInstance()
        toAdd.filter {
            it !in couldNotBeDownloaded
        }.forEachIndexed { i, entryToAdd ->
            try {
                transporter.transport(createPathFromYTPLEntry(cache,entryToAdd),destOnDevice)
            }catch(e:Exception){
                println("Could not transport ${toAdd[i]}. The exception was:\n$e")
            }finally {
                println("$i of ${toAdd.size} files transported")
            }
        }
    }

    private fun removeEntries(toRemove:List<YTPlaylistEntry>){
        println("Starting to remove files. Entries to add are:\n$toRemove")
        toRemove.forEachIndexed { i, fileToRemove ->
            try {
                deleteFileOnDevice(createPathFromYTPLEntry(destOnDevice,fileToRemove,true))
                println("$i of ${toRemove.size} files removed")
            }catch(e:Exception){
                println("Could not remove ${toRemove[i]}. The exception was:\n$e")
            }
        }
    }

    private fun createPathFromYTPLEntry(base:Path,entry:YTPlaylistEntry,addQuotesAroundFilename:Boolean = false):Path{
        val quoteOrNot = if (addQuotesAroundFilename) "'" else ""
        return Paths.get("$base/$quoteOrNot${YT_DL_FILE_NAME_FORMAT.format(entry.title,entry.ytID)}$quoteOrNot")
    }

    private fun clearCache(){
        Files.walk(cache).toList().drop(1).forEach {
            Files.delete(it)
        }
    }
}