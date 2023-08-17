package app.suhasdissa.mellowmusic.backend.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao {
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSong(song: Song)

    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSongs(songs: List<Song>)

    @Delete(entity = Song::class)
    fun removeSong(song: Song)

    @Query("SELECT * from Song WHERE id=:id")
    fun getSongById(id: String): Song?

    @Query("SELECT * from Song")
    fun getAllSongs(): List<Song>

    @Query("SELECT * from Song WHERE likedAt IS NOT NULL")
    fun getFavSongs(): List<Song>

    @Query("SELECT * from Song ORDER BY title ASC")
    fun getAllSongsStream(): Flow<List<Song>>

    @Query("SELECT * from Song WHERE likedAt IS NOT NULL ORDER BY likedAt DESC")
    fun getFavSongsStream(): Flow<List<Song>>

    @Query("SELECT * from Song ORDER BY id DESC LIMIT :limit")
    fun getRecentSongs(limit: Int): List<Song>

    @Query("SELECT * FROM Song WHERE title LIKE :search OR artistsText LIKE :search")
    fun search(search: String): List<Song>
}
