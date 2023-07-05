package app.suhasdissa.mellowmusic.utils

object Pref {
    const val pipedInstanceKey = "PipedInstanceKey"

    val pipedInstances = listOf(
        "kavin.rocks" to "https://pipedapi.kavin.rocks/",
        "tokhmi,xyz" to "https://pipedapi.tokhmi.xyz/",
        "whatever.social" to "https://watchapi.whatever.social/",
        "moomoo.me" to "https://pipedapi.moomoo.me/",
        "syncpundit.io" to "https://pipedapi.syncpundit.io/",
        "mha.fi" to "https://api-piped.mha.fi/",
        "garudalinux.org" to "https://piped-api.garudalinux.org/",
        "rivo.io" to "https://pipedapi.rivo.lol/",
        "aeong.one" to "https://pipedapi.aeong.one/",
        "leptons.xyz" to "https://pipedapi.leptons.xyz/",
        "lunar.icu" to "https://piped-api.lunar.icu/",
        "dc09.ru" to "https://ytapi.dc09.ru/",
        "colinslegacy.com" to "https://pipedapi.colinslegacy.com/",
        "vyper.me" to "https://yapi.vyper.me/"
    )

    var pipedUrl: Int? = null
}
