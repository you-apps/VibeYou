package app.suhasdissa.vibeyou.backend.models.playlists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Playlists(
    @SerialName("items") var items: ArrayList<Playlist> = arrayListOf()
)
