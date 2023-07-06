package app.suhasdissa.mellowmusic.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.Items
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse

val Items.asSong: Song
    get() = Song(
        id = videoId,
        title = title,
        artistsText = uploaderName,
        durationText = DateUtils.formatElapsedTime(duration.toLong()),
        thumbnailUrl = thumbnail
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
        val pipedUrl = Pref.pipedUrl?.let { Pref.pipedInstances[it] } ?: Pref.pipedInstances.first()
        return "${pipedUrl}vi_webp/${mediaId}/maxresdefault.webp?host=i.ytimg.com"
    }