package app.suhasdissa.mellowmusic.backend.viewmodel.state

import app.suhasdissa.mellowmusic.backend.data.Song

sealed interface PlaylistInfoState {
    object Loading : PlaylistInfoState
    object Error : PlaylistInfoState
    data class Success(
        val name: String,
        val thumbnail: String?,
        val songs: List<Song>

    ) : PlaylistInfoState
}
