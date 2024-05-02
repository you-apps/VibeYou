package app.suhasdissa.vibeyou.backend.models.songs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongItem(
    @SerialName("url") var url: String = "",
    @SerialName("title") var title: String = "",
    @SerialName("thumbnail") var thumbnail: String = "",
    @SerialName("uploaderName") var uploaderName: String = "",
    @SerialName("duration") var duration: Int = 0
) {
    val videoId
        get() = url.replace("/watch?v=", "")
}
