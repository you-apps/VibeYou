package app.suhasdissa.mellowmusic.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import app.suhasdissa.mellowmusic.backend.data.Album
import app.suhasdissa.mellowmusic.backend.data.Artist
import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.database.entities.SongEntity
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist
import app.suhasdissa.mellowmusic.backend.models.songs.SongItem

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

val app.suhasdissa.mellowmusic.backend.models.artists.Artist.asArtist: Artist
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
                    bundleOf("isFavourite" to isFavourite)
                )
                .build()
        )
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
