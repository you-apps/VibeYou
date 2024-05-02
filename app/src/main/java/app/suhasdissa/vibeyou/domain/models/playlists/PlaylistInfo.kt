package app.suhasdissa.vibeyou.backend.models.playlists

import app.suhasdissa.vibeyou.backend.models.songs.SongItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistInfo(
    @SerialName("name") var name: String = "",
    @SerialName("thumbnailUrl") var thumbnailUrl: String? = null,
    @SerialName("relatedStreams") var relatedStreams: ArrayList<SongItem> = arrayListOf()
)
