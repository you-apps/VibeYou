package app.suhasdissa.mellowmusic.backend.models.artists

import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelTabResponse(
    @SerialName("content") var content: List<Playlist> = emptyList()
)
