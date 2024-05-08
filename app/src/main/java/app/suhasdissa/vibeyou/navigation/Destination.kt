package app.suhasdissa.vibeyou.navigation

import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data class Home(val destination: Int) : Destination()

    @Serializable
    object OnlineSearch : Destination()

    @Serializable
    object LocalSearch : Destination()

    @Serializable
    object Settings : Destination()

    @Serializable
    object About : Destination()

    @Serializable
    object NetworkSettings : Destination()

    @Serializable
    object DatabaseSettings : Destination()

    @Serializable
    object AppearanceSettings : Destination()

    @Serializable
    data class Playlists(val album: Album) : Destination()

    @Serializable
    data class LocalPlaylists(val album: Album) : Destination()

    @Serializable
    data class SavedPlaylists(val album: Album) : Destination()

    @Serializable
    data class OnlineArtist(val artist: Artist) : Destination()

    @Serializable
    data class LocalArtist(val artist: Artist) : Destination()
}

@Serializable
sealed class HomeDestination(val destination: Int) {
    @Serializable
    object LocalMusic : HomeDestination(destination = 0)

    @Serializable
    object OnlineMusic : HomeDestination(destination = 1)
}