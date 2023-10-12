package app.suhasdissa.vibeyou.backend.viewmodel.state

import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.data.Artist

sealed interface ArtistInfoState {
    object Loading : ArtistInfoState
    object Error : ArtistInfoState
    data class Success(
        val artist: Artist,
        val playlists: List<Album>

    ) : ArtistInfoState
}
