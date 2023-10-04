package app.suhasdissa.mellowmusic.backend.data

import android.net.Uri

data class AlbumInfo(
    val name: String,
    val thumbnailUri: Uri? = null,
    val songs: List<Song>
)
