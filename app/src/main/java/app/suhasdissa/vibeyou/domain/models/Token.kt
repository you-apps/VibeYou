package app.suhasdissa.vibeyou.backend.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @SerialName("token") val token: String? = null,
    @SerialName("error") val error: String? = null
)
