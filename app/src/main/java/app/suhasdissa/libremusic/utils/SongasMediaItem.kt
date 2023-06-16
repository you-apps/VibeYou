package app.suhasdissa.libremusic.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import app.suhasdissa.libremusic.backend.database.entities.Song
import app.suhasdissa.libremusic.backend.models.Items

val Items.asSong: Song
    get() = Song(
        id = url.replace("/watch?v=", ""),
        title = title,
        artistsText = uploaderName,
        durationText = DateUtils.formatElapsedTime(duration.toLong()),
        thumbnailUrl = thumbnail
    )

val Song.asMediaItem: MediaItem
    @SuppressLint("UnsafeOptInUsageError")
    get() = MediaItem.Builder()
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artistsText)
                .setArtworkUri(thumbnailUrl?.toUri())
                .build()
        )
        .setMediaId(id)
        .setCustomCacheKey(id)
        .build()