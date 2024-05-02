package app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state

import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist

sealed interface ArtistInfoState {
    object Loading : ArtistInfoState
    object Error : ArtistInfoState
    data class Success(
        val artist: Artist,
        val playlists: List<Album>

    ) : ArtistInfoState
}
