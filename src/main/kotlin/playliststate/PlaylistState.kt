package playliststate

interface PlaylistState{
    var entries:List<PlaylistEntry>

    fun update()
}
