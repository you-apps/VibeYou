package app.suhasdissa.vibeyou.backend.models.hyper

import kotlinx.serialization.Serializable

@Serializable
data class NextSongsResponse(
    val lyricsId: String,
    val mediaSession: MediaSession,
    val songs: List<Song>
)