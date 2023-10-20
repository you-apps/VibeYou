package app.suhasdissa.vibeyou

sealed class Destination(val route: String) {
    object PipedMusic : Destination("piped_music")
    object LocalMusic : Destination("local_music")
    object OnlineSearch : Destination("online_search")
    object LocalSearch : Destination("local_search")
    object Settings : Destination("settings")
    object About : Destination("about")
    object NetworkSettings : Destination("net_settings")
    object DatabaseSettings : Destination("database_settings")
    object AppearanceSettings : Destination("appearance_settings")
    object Playlists : Destination("playlist_screen")
    object LocalPlaylists : Destination("local_playlist_screen")
    object Artist : Destination("artist")
    object LocalArtist : Destination("local_artist")
}
