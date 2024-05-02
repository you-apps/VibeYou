package app.suhasdissa.vibeyou.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.suhasdissa.vibeyou.data.database.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao {
    @Insert(entity = SongEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSong(song: SongEntity)

    @Insert(entity = SongEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSongs(songs: List<SongEntity>)

    @Delete(entity = SongEntity::class)
    fun removeSong(song: SongEntity)

    @Delete(entity = SongEntity::class)
    fun removeSongs(song: List<SongEntity>)

    @Query("SELECT * from song WHERE id=:id")
    fun getSongById(id: String): SongEntity?

    @Query("SELECT * from song")
    fun getAllSongs(): List<SongEntity>

    @Query("SELECT * from song WHERE likedAt IS NOT NULL")
    fun getFavSongs(): List<SongEntity>

    @Query("SELECT * from song ORDER BY title ASC")
    fun getAllSongsStream(): Flow<List<SongEntity>>

    @Query("SELECT * from song WHERE likedAt IS NOT NULL ORDER BY title ASC")
    fun getFavSongsStream(): Flow<List<SongEntity>>

    @Query("SELECT * from song ORDER BY id DESC LIMIT :limit")
    fun getRecentSongs(limit: Int): List<SongEntity>

    @Query("SELECT * from song WHERE title LIKE :search OR artistsText LIKE :search")
    fun search(search: String): List<SongEntity>
}
