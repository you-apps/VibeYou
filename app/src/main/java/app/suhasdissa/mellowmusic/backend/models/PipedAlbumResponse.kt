package app.suhasdissa.mellowmusic.backend.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PipedSongAlbumResponse(
    @SerialName("items") var items: ArrayList<AlbumItems> = arrayListOf()
)

@Serializable
data class AlbumItems(
    @SerialName("url") var url: String = "",
    @SerialName("type") var type: String = "",
    @SerialName("name") var name: String = "",
    @SerialName("thumbnail") var thumbnail: String = "",
    @SerialName("uploaderName") var uploaderName: String = ""
) {
    val playlistId
        get() = url.replace("/playlist?list=", "")
}