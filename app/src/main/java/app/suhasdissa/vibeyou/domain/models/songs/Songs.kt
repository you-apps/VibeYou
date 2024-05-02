package app.suhasdissa.vibeyou.backend.models.songs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Songs(
    @SerialName("items") var items: ArrayList<SongItem> = arrayListOf()
)
