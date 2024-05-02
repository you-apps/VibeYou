package app.suhasdissa.vibeyou.backend.models.artists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("url") var url: String,
    @SerialName("name") var name: String,
    @SerialName("thumbnail") var thumbnail: String? = null,
    @SerialName("description") var description: String? = null
) {
    val artistId
        get() = url.replace("/channel/", "")
}
