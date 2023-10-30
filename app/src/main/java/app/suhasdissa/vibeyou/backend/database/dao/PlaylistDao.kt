package app.suhasdissa.vibeyou.backend.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.suhasdissa.vibeyou.backend.database.entities.PlaylistEntity
import app.suhasdissa.vibeyou.backend.database.entities.PlaylistWithSongs
import app.suhasdissa.vibeyou.backend.database.entities.SongPlaylistMap
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylist(playlist: PlaylistEntity)

    @Insert(entity = SongPlaylistMap::class, onConflict = OnConflictStrategy.REPLACE)
    fun addPlaylistMaps(maps: List<SongPlaylistMap>)

    @Query("SELECT * from playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * from playlists WHERE id=:id")
    fun getPlaylist(id: String): PlaylistWithSongs

    @Delete(entity = PlaylistEntity::class)
    fun removePlaylist(playlist: PlaylistEntity)
}
