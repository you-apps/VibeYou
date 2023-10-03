package app.suhasdissa.mellowmusic.backend.data

import android.graphics.Bitmap

data class Album(
    val id: String,
    val title: String,
    val thumbnail: Bitmap? = null,
    val thumbnailUrl: String? = null,
    val artistsText: String
)
