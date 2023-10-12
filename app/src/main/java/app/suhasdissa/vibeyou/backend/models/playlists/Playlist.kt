package app.suhasdissa.vibeyou.backend.models.playlists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    @SerialName("url") var url: String = "",
    @SerialName("type") var type: String = "",
    @SerialName("name") var name: String = "",
    @SerialName("thumbnail") var thumbnail: String = "",
    @SerialName("uploaderName") var uploaderName: String = ""
) {
    val playlistId
        get() = url.replace("/playlist?list=", "")
}
