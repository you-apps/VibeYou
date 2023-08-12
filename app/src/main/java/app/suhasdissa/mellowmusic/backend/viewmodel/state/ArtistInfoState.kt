package app.suhasdissa.mellowmusic.backend.viewmodel.state

import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist

sealed interface ArtistInfoState {
    object Loading : ArtistInfoState
    object Error : ArtistInfoState
    data class Success(
        val name: String,
        val thumbnail: String?,
        val description: String?,
        val playlists: List<Playlist>

    ) : ArtistInfoState
}
