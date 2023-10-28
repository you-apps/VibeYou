package app.suhasdissa.vibeyou.backend.data

import android.net.Uri

data class Artist(
    val id: String,
    val artistsText: String,
    val thumbnailUri: Uri? = null,
    val description: String? = null,
    val numberOfTracks: Int? = null,
    val numberOfAlbums: Int? = null
)
