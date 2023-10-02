package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.database.entities.Song
import kotlinx.coroutines.flow.Flow

interface SongDatabaseRepository {
    suspend fun addSong(song: Song)
    suspend fun removeSong(song: Song)
    suspend fun getSongById(id: String): Song?
    fun getAllSongsStream(): Flow<List<Song>>
    fun getFavSongsStream(): Flow<List<Song>>
}
