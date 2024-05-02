package app.suhasdissa.vibeyou.backend.models.hyper

import kotlinx.serialization.Serializable

@Serializable
data class MediaSession(
    val album: String,
    val thumbnails: List<Thumbnail>
)