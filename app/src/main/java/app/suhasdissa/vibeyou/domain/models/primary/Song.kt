package app.suhasdissa.vibeyou.domain.models.primary

import android.net.Uri

data class Song(
    val id: String,
    val title: String,
    val artistsText: String? = null,
    val durationText: String?,
    val likedAt: Long? = null,
    val thumbnailUri: Uri? = null,
    val albumId: Long? = null,
    val artistId: Long? = null,
    val isLocal: Boolean = false,
    val creationDate: Long? = null,
    val dateAdded: Long? = null,
    val trackNumber: Long? = null
) {
    fun toggleLike(): Song {
        return copy(
            likedAt = if (likedAt == null) System.currentTimeMillis() else null
        )
    }

    val isFavourite
        get() = likedAt != null
}
