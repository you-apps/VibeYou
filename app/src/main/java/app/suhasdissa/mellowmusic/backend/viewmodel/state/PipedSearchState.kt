package app.suhasdissa.mellowmusic.backend.viewmodel.state

import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.artists.Artist
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist

sealed interface PipedSearchState {
    sealed interface Success : PipedSearchState {
        data class Songs(val items: List<Song>) : Success
        data class Playlists(val items: List<Playlist>) : Success
        data class Artists(val items: List<Artist>) : Success
    }

    data class Error(val error: String) : PipedSearchState
    object Loading : PipedSearchState
    object Empty : PipedSearchState
}
