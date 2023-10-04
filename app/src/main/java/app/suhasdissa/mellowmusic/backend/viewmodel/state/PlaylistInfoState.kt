package app.suhasdissa.mellowmusic.backend.viewmodel.state

import android.net.Uri
import app.suhasdissa.mellowmusic.backend.data.Song

sealed interface PlaylistInfoState {
    object Loading : PlaylistInfoState
    object Error : PlaylistInfoState
    data class Success(
        val name: String,
        val thumbnail: Uri?,
        val songs: List<Song>

    ) : PlaylistInfoState
}
