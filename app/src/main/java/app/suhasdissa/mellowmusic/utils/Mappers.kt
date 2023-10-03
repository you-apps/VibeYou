package app.suhasdissa.mellowmusic.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.database.entities.SongEntity
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse
import app.suhasdissa.mellowmusic.backend.models.songs.SongItem

val SongItem.asSong: Song
    get() = Song(
        id = videoId,
        title = title,
        artistsText = uploaderName,
        durationText = DateUtils.formatElapsedTime(duration.toLong()),
        thumbnailUrl = thumbnail
    )

val Song.asSongEntity: SongEntity
    get() = SongEntity(
        id = id,
        title = title,
        artistsText = artistsText,
        durationText = durationText,
        thumbnailUrl = thumbnailUrl,
        likedAt = likedAt
    )

val SongEntity.asSong: Song
    get() = Song(
        id = id,
        title = title,
        artistsText = artistsText,
        durationText = durationText,
        thumbnailUrl = thumbnailUrl,
        likedAt = likedAt
    )

fun PipedSongResponse.asSong(id: String): Song {
    return Song(
        id = id,
        title = title!!,
        artistsText = uploader ?: "",
        durationText = DateUtils.formatElapsedTime(duration?.toLong() ?: 0L),
        thumbnailUrl = thumbnailUrl ?: ""
    )
}

val Song.asMediaItem: MediaItem
    @SuppressLint("UnsafeOptInUsageError")
    get() = MediaItem.Builder()
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artistsText)
                .setArtworkUri(thumbnailUrl?.toUri())
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
        val pipedUrl = Pref.pipedInstances.getOrNull(Pref.pipedUrl)?.proxyUrl
            ?: "https://piped-proxy.lunar.icu/"
        return "${pipedUrl}vi_webp/$mediaId/maxresdefault.webp?host=i.ytimg.com"
    }
