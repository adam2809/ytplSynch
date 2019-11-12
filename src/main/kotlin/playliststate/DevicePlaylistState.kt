package playliststate

import utils.getTestFilesOnDeviceFromDir
import java.lang.Exception

class DevicePlaylistState(private val dir:String):PlaylistState{
//    TODO no idea how to make the setter private so pls don't use it its not supposed to be used
    override var entries: List<PlaylistEntry> = emptyList()

    init {
        update()
    }

    override fun update() {
        entries = getTestFilesOnDeviceFromDir(dir).map(::extractPlaylistEntryFilename)
    }

    private fun extractPlaylistEntryFilename(filename:String):YTPlaylistEntry{
        return filename.let {
            val title = it.substringBeforeLast('-')
            val ytId = it.substringAfterLast('-').substringBeforeLast('.')
            YTPlaylistEntry(ytId,title)
        }
    }
}