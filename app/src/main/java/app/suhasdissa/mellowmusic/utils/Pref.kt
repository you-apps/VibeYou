package app.suhasdissa.mellowmusic.utils

import app.suhasdissa.mellowmusic.backend.models.PipedInstance

object Pref {
    const val pipedInstanceKey = "PipedInstanceKey"
    const val authTokenKey = "AuthTokenKey"

    val pipedInstances = listOf(
        PipedInstance(
            "kavin.rocks",
            "https://pipedapi.kavin.rocks/",
            "pipedapi.kavin.rocks"
        ),
        PipedInstance(
            "lunar.icu",
            "https://piped-api.lunar.icu/",
            "piped-api.lunar.icu",
            "https://piped-proxy.lunar.icu/"
        ),
        PipedInstance(
            "whatever.social",
            "https://watchapi.whatever.social/",
            "watchapi.whatever.social",
            "https://watchproxy-nl.whatever.social"
        ),
        PipedInstance(
            "tokhmi.xyz",
            "https://pipedapi.tokhmi.xyz/",
            "pipedapi.tokhmi.xyz"
        ),
        PipedInstance(
            "mha.fi",
            "https://api-piped.mha.fi/",
            "api-piped.mha.fi"
        ),
        PipedInstance(
            "garudalinux.org",
            "https://piped-api.garudalinux.org/",
            "piped-api.garudalinux.org"
        ),
        PipedInstance(
            "piped.yt",
            "https://api.piped.yt/",
            "api.piped.yt"
        )
    )
    var pipedUrl = 0
    var currentInstance: String =
        (pipedInstances.getOrNull(pipedUrl) ?: pipedInstances.first()).instance
}
