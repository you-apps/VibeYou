package app.suhasdissa.mellowmusic

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(val route: String) {
    object PipedMusic : Destination("piped_music")
    object LocalMusic : Destination("local_music")
    object YoutubeMusic : Destination("yt_music")
    object Search : Destination("search") {
        const val arg = "is_online"
        val routeWithArgs = "$route/{$arg}"
        val args = listOf(
            navArgument(
                arg
            ) {
                type = NavType.BoolType
            }
        )
    }

    object Settings : Destination("settings")
    object About : Destination("about")
    object NetworkSettings : Destination("net_settings")
    object DatabaseSettings : Destination("database_settings")
    object Playlists : Destination("playlist_screen")
    object Artist : Destination("artist")
}
