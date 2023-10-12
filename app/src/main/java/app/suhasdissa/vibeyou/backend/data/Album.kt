package app.suhasdissa.vibeyou.backend.data

import android.net.Uri

data class Album(
    val id: String,
    val title: String,
    val thumbnailUri: Uri? = null,
    val artistsText: String,
    val numberOfSongs: Int? = null
)
