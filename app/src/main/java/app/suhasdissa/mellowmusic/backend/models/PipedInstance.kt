package app.suhasdissa.mellowmusic.backend.models

data class PipedInstance(
    val name: String,
    val url: String,
    val instance: String,
    val proxyUrl: String? = null
)
