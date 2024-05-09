package app.suhasdissa.vibeyou.navigation

import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    object OnlineMusic : Destination()

    @Serializable
    object LocalMusic : Destination()

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
