package app.suhasdissa.vibeyou.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.suhasdissa.vibeyou.domain.models.primary.Album

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: Album.Type,
    val subTitle: String? = null,
    val thumbnailUrl: String? = null
)
