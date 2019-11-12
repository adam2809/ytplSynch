package playliststate

import utils.runCommand
import java.io.File

class YTPlaylistState(ytURL:String):PlaylistState{
    override var entries: List<PlaylistEntry> = arrayListOf()

    val PL_INFO_COMMAND = arrayOf(
        "youtube-dl",
        "--dump-single-json",
        "--flat-playlist",
        ytURL
    )

    init {
        update()
    }

    override fun update() {
        val (output,error) = PL_INFO_COMMAND.runCommand(File("."))

    }
}


