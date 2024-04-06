package app.suhasdissa.vibeyou.backend.models.hyper

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val height: Int,
    val url: String,
    val width: Int
)