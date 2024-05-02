package app.suhasdissa.vibeyou.data.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        entity = SongEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SongPlaylistMap::class,
            parentColumn = "playlistId",
            entityColumn = "songId"
        )
    )
    val songs: List<SongEntity> = listOf()
)
