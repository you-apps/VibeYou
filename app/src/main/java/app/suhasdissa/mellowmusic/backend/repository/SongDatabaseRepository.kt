package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.database.entities.SongEntity
import kotlinx.coroutines.flow.Flow

interface SongDatabaseRepository {
    suspend fun addSong(song: Song)
    suspend fun removeSong(song: Song)
    suspend fun getSongById(id: String): Song?
    fun getAllSongsStream(): Flow<List<SongEntity>>
    fun getFavSongsStream(): Flow<List<SongEntity>>
}
