package app.suhasdissa.vibeyou.backend.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Login(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String
)
