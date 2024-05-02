package app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state

import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Song

sealed interface AlbumInfoState {
    object Loading : AlbumInfoState
    object Error : AlbumInfoState
    data class Success(
        val album: Album,
        val songs: List<Song>

    ) : AlbumInfoState
}
