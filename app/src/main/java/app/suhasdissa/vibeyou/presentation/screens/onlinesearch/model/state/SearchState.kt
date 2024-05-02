package app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state

import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import app.suhasdissa.vibeyou.domain.models.primary.Song

sealed interface SearchState {
    sealed interface Success : SearchState {
        data class Songs(val items: List<Song>) : Success
        data class Playlists(val items: List<Album>) : Success
        data class Artists(val items: List<Artist>) : Success
    }

    data class Error(val error: String) : SearchState
    object Loading : SearchState
    object Empty : SearchState
}
