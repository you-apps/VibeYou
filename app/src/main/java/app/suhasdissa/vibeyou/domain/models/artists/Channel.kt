package app.suhasdissa.vibeyou.backend.models.artists

import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val id: String? = null,
    val name: String? = null,
    val avatarUrl: String? = null,
    val bannerUrl: String? = null,
    val description: String? = null,
    val tabs: List<ChannelTab> = emptyList()
)

@Serializable
data class ChannelTab(
    val name: String,
    val data: String
)
