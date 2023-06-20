package app.suhasdissa.mellowmusic.backend.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.suhasdissa.mellowmusic.backend.database.entities.Song

@Dao
interface SongsDao {
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSong(song: Song)

    @Query("SELECT * from Song WHERE id=:id")
    fun getSongById(id: String): Song?

    @Query("SELECT * from Song")
    fun getAllSongs(): List<Song>

    @Query("SELECT * from Song WHERE likedAt IS NOT NULL")
    fun getFavSongs(): List<Song>

    @Query("SELECT * from Song ORDER BY id DESC LIMIT :limit")
    fun getRecentSongs(limit:Int): List<Song>
}