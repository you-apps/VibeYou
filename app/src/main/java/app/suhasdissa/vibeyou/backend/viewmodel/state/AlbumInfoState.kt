package app.suhasdissa.vibeyou.backend.viewmodel.state

import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.data.Song

sealed interface AlbumInfoState {
    object Loading : AlbumInfoState
    object Error : AlbumInfoState
    data class Success(
        val album: Album,
        val songs: List<Song>

    ) : AlbumInfoState
}
