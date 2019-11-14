package playliststate

import utils.runCommand
import java.io.File

import org.json.*

class YTPlaylistState(ytURL:String):PlaylistState{
    override var entries: List<PlaylistEntry> = arrayListOf()

    val PL_INFO_COMMAND = arrayOf(
        "youtube-dl",
        "--dump-single-json",
        "--flat-playlist",
        ytURL
    )

    override fun update() {
        val (output,_) = PL_INFO_COMMAND.runCommand(File("."))
        entries = JSONObject(output).getJSONArray("entries").map {
            val entryJSON = (it as JSONObject)
            YTPlaylistEntry(entryJSON.getString("id"),entryJSON.getString("title"))
        }
    }
}


