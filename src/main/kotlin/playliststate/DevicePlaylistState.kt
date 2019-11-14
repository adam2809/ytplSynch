package playliststate

import utils.getFilesOnDeviceFromPath
import java.nio.file.Path
import java.nio.file.Paths

class DevicePlaylistState(private val path: Path): PlaylistState() {
//    TODO no idea how to make the setter private so pls don't use it its not supposed to be used
    override var entries: List<YTPlaylistEntry> = emptyList()

    override fun update() {
        entries = getFilesOnDeviceFromPath(path).map(::extractPlaylistEntryFilename)
    }

    private fun extractPlaylistEntryFilename(filename:String):YTPlaylistEntry{
        return filename.let {
            val title = it.substringBeforeLast('-')
            val ytId = it.substringAfterLast('-').substringBeforeLast('.')
            YTPlaylistEntry(ytId,title)
        }
    }
}