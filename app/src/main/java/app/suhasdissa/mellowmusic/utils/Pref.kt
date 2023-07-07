package app.suhasdissa.mellowmusic.utils

object Pref {
    const val pipedInstanceKey = "PipedInstanceKey"

    val pipedInstances = listOf(
        "lunar.icu" to "https://piped-api.lunar.icu/",
        "whatever.social" to "https://watchapi.whatever.social/",
        "tokhmi.xyz" to "https://pipedapi.tokhmi.xyz/",
        "mha.fi" to "https://api-piped.mha.fi/",
        "garudalinux.org" to "https://piped-api.garudalinux.org/",
        "piped.yt" to "https://api.piped.yt/",
        "kavin.rocks" to "https://pipedapi.kavin.rocks/",
    )

    var pipedUrl: Int? = null
}
