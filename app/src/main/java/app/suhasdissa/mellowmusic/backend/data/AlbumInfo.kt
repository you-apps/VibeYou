package app.suhasdissa.mellowmusic.backend.data

import android.graphics.Bitmap

data class AlbumInfo(
    val name: String,
    val thumbnailUrl: String? = null,
    val thumbnail: Bitmap? = null,
    val songs: List<Song>
)
