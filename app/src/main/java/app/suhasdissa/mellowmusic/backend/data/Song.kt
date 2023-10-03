package app.suhasdissa.mellowmusic.backend.data

import android.graphics.Bitmap

data class Song(
    val id: String,
    val title: String,
    val artistsText: String? = null,
    val durationText: String?,
    val thumbnailUrl: String? = null,
    val likedAt: Long? = null,
    val thumbnail: Bitmap? = null,
    val album: String? = null
) {
    fun toggleLike(): Song {
        return copy(
            likedAt = if (likedAt == null) System.currentTimeMillis() else null
        )
    }

    val isFavourite
        get() = likedAt != null
}
