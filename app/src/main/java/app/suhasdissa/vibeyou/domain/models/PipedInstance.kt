package app.suhasdissa.vibeyou.backend.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PipedInstance(
    val name: String,
    @SerialName("api_url") val apiUrl: String,
    val cache: Boolean = false,
    val cdn: Boolean = false,
    @SerialName("image_proxy_url") val imageProxyUrl: String = "",
    @SerialName("last_checked") val lastChecked: Int = 0,
    val locations: String = "",
    val registered: Int = 0,
    @SerialName("registration_disabled") val registrationDisabled: Boolean = false,
    @SerialName("s3_enabled") val s3Enabled: Boolean = false,
    @SerialName("up_to_date") val upToDate: Boolean = false,
    val version: String = ""
) {
    val netLoc = apiUrl.replace("https://", "")
}
