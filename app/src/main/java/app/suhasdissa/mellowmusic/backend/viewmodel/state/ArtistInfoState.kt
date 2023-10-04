package app.suhasdissa.mellowmusic.backend.viewmodel.state

import app.suhasdissa.mellowmusic.backend.data.Album
import app.suhasdissa.mellowmusic.backend.data.Artist

sealed interface ArtistInfoState {
    object Loading : ArtistInfoState
    object Error : ArtistInfoState
    data class Success(
        val artist: Artist,
        val playlists: List<Album>

    ) : ArtistInfoState
}
