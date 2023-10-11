package app.suhasdissa.mellowmusic.backend.models.artists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artists(
    @SerialName("items") var items: List<Artist> = listOf()
)
