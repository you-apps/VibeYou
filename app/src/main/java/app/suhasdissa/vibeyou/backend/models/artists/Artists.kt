package app.suhasdissa.vibeyou.backend.models.artists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artists(
    @SerialName("items") var items: ArrayList<Artist> = arrayListOf()
)
