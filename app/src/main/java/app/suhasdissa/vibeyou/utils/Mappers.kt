package app.suhasdissa.vibeyou.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import app.suhasdissa.vibeyou.backend.models.PipedSongResponse
import app.suhasdissa.vibeyou.backend.models.playlists.Playlist
import app.suhasdissa.vibeyou.backend.models.songs.SongItem
import app.suhasdissa.vibeyou.data.database.entities.PlaylistEntity
import app.suhasdissa.vibeyou.data.database.entities.SongEntity
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import app.suhasdissa.vibeyou.domain.models.primary.Song

val SongItem.asSong: Song
    get() = Song(
        id = videoId,
        title = title,
        artistsText = uploaderName,
        durationText = DateUtils.formatElapsedTime(duration.toLong()),
        thumbnailUri = thumbnail.toUri()
    )

val Song.asSongEntity: SongEntity
    get() = SongEntity(
        id = id,
        title = title,
        artistsText = artistsText,
        durationText = durationText,
        thumbnailUrl = thumbnailUri.toString(),
        likedAt = likedAt
    )

val SongEntity.asSong: Song
    get() = Song(
        id = id,
        title = title,
        artistsText = artistsText,
        durationText = durationText,
        thumbnailUri = thumbnailUrl?.toUri(),
        likedAt = likedAt
    )

fun PipedSongResponse.asSong(id: String): Song {
    return Song(
        id = id,
        title = title!!,
        artistsText = uploader ?: "",
        durationText = DateUtils.formatElapsedTime(duration?.toLong() ?: 0L),
        thumbnailUri = thumbnailUrl?.toUri()
    )
}

val Playlist.asAlbum: Album
    get() = Album(
        id = playlistId,
        title = name,
        artistsText = uploaderName,
        thumbnailUri = thumbnail.toUri()
    )

val Album.asPlaylistEntity: PlaylistEntity
    get() = PlaylistEntity(
        id = id,
        title = title,
        type = type,
        subTitle = artistsText,
        thumbnailUrl = thumbnailUri.toString()
    )
val PlaylistEntity.asAlbum: Album
    get() = Album(
        id = id,
        title = title,
        thumbnailUri = thumbnailUrl?.toUri(),
        artistsText = subTitle ?: "",
        isLocal = true,
        type = type
    )
val app.suhasdissa.vibeyou.backend.models.artists.Artist.asArtist: Artist
    get() = Artist(
        id = artistId,
        thumbnailUri = thumbnail?.toUri(),
        description = description,
        artistsText = name
    )

val Song.asMediaItem: MediaItem
    @SuppressLint("UnsafeOptInUsageError")
    get() = MediaItem.Builder()
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artistsText)
                .setArtworkUri(thumbnailUri)
                .setExtras(
                    bundleOf(IS_LOCAL_KEY to isLocal)
                )
                .build()
        )
        .setUri(id)
        .setMediaId(id)
        .setCustomCacheKey(id)
        .build()

val MediaItem.maxResThumbnail: String
    get() {
        if (mediaId.startsWith("content")) return ""
        val pipedProxyUrl =
            Pref.currentInstance.imageProxyUrl.ifEmpty { "https://pipedproxy.kavin.rocks" }
        return "$pipedProxyUrl/vi_webp/$mediaId/maxresdefault.webp?host=i.ytimg.com"
    }

const val IS_LOCAL_KEY = "isLocal"
