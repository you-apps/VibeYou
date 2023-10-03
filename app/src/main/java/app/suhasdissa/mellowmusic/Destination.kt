package app.suhasdissa.mellowmusic

sealed class Destination(val route: String) {
    object PipedMusic : Destination("piped_music")
    object LocalMusic : Destination("local_music")
    object YoutubeMusic : Destination("yt_music")
    object OnlineSearch : Destination("online_search")
    object LocalSearch : Destination("local_search")
    object Settings : Destination("settings")
    object About : Destination("about")
    object NetworkSettings : Destination("net_settings")
    object DatabaseSettings : Destination("database_settings")
    object Playlists : Destination("playlist_screen")
    object Artist : Destination("artist")
}
