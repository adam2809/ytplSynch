package playliststate

abstract class PlaylistState{
    abstract var entries:List<YTPlaylistEntry>

    abstract fun update()

    operator fun minus(state: PlaylistState):List<YTPlaylistEntry>{
        return entries.filter { !state.entries.contains(it) }
    }

    operator fun get(i:Int):YTPlaylistEntry{
        return entries[i]
    }
}
