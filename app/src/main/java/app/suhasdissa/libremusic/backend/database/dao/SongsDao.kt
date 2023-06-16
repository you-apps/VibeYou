package app.suhasdissa.libremusic.backend.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.suhasdissa.libremusic.backend.database.entities.Song

@Dao
interface SongsDao {
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSong(song: Song)

    @Query("SELECT * from Song")
    fun getAllSongs(): List<Song>

    @Query("SELECT * from Song WHERE likedAt IS NOT NULL")
    fun getFavSongs(): List<Song>
}