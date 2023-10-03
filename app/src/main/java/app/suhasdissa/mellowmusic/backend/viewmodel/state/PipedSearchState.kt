package app.suhasdissa.mellowmusic.backend.viewmodel.state

import app.suhasdissa.mellowmusic.backend.data.Album
import app.suhasdissa.mellowmusic.backend.data.Artist
import app.suhasdissa.mellowmusic.backend.data.Song

sealed interface PipedSearchState {
    sealed interface Success : PipedSearchState {
        data class Songs(val items: List<Song>) : Success
        data class Playlists(val items: List<Album>) : Success
        data class Artists(val items: List<Artist>) : Success
    }

    data class Error(val error: String) : PipedSearchState
    object Loading : PipedSearchState
    object Empty : PipedSearchState
}
