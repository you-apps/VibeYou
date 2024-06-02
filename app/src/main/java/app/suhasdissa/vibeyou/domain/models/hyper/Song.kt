package app.suhasdissa.vibeyou.backend.models.hyper

import androidx.core.net.toUri
import app.suhasdissa.vibeyou.data.database.entities.SongEntity
import app.suhasdissa.vibeyou.domain.models.primary.Song
import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val id: String,
    val subtitle: String,
    val thumbnails: List<Thumbnail>,
    val title: String
) {
    val asSong: Song
        get() {
            return Song(
                id = id,
                title = title,
                artistsText = subtitle,
                durationText = null,
                thumbnailUri = thumbnails.maxByOrNull { it.height }?.url?.toUri()
            )
        }

    val asSongEntity: SongEntity
        get() {
            return SongEntity(
                id = id,
                title = title,
                artistsText = subtitle,
                durationText = null,
                thumbnailUrl = thumbnails.maxByOrNull { it.height }?.url
            )
        }
}