package app.suhasdissa.vibeyou.navigation

import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data object OnlineMusic : Destination()

    @Serializable
    data object LocalMusic : Destination()

    @Serializable
    data object OnlineSearch : Destination()

    @Serializable
    data object LocalSearch : Destination()

    @Serializable
    data object Settings : Destination()

    @Serializable
    data object About : Destination()

    @Serializable
    data object NetworkSettings : Destination()

    @Serializable
    data object DatabaseSettings : Destination()

    @Serializable
    data object AppearanceSettings : Destination()

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
