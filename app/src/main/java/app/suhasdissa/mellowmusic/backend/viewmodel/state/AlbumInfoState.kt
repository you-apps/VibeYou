package app.suhasdissa.mellowmusic.backend.viewmodel.state

import app.suhasdissa.mellowmusic.backend.data.Album
import app.suhasdissa.mellowmusic.backend.data.Song

sealed interface AlbumInfoState {
    object Loading : AlbumInfoState
    object Error : AlbumInfoState
    data class Success(
        val album: Album,
        val songs: List<Song>

    ) : AlbumInfoState
}
