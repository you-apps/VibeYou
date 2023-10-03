package app.suhasdissa.mellowmusic.backend.data

import android.graphics.Bitmap

data class Artist(
    val id: String,
    val artistsText: String,
    val thumbnail: Bitmap? = null,
    val thumbnailUrl: String? = null,
    val description: String? = null
)
