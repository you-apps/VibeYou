package app.suhasdissa.mellowmusic

sealed class Destination(val route: String) {
    object PipedMusic : Destination("piped_music")
    object LocalMusic : Destination("local_music")
    object YoutubeMusic : Destination("yt_music")
    object Search : Destination("search")
    object Settings : Destination("settings")
    object About : Destination("about")
    object NetworkSettings : Destination("net_settings")
    object DatabaseSettings : Destination("database_settings")
    object Playlists : Destination("playlist_screen")
    object Artist : Destination("artist")
}
