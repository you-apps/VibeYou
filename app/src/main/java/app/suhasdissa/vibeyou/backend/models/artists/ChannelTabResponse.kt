package app.suhasdissa.vibeyou.backend.models.artists

import app.suhasdissa.vibeyou.backend.models.playlists.Playlist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelTabResponse(
    @SerialName("content") var content: List<Playlist> = emptyList()
)
