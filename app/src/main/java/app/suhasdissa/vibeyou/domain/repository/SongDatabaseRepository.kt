package app.suhasdissa.vibeyou.backend.repository

import app.suhasdissa.vibeyou.data.database.entities.SongEntity
import app.suhasdissa.vibeyou.domain.models.primary.Song
import kotlinx.coroutines.flow.Flow

interface SongDatabaseRepository {
    suspend fun addSong(song: Song)
    suspend fun addSongs(songs: List<Song>)
    suspend fun removeSong(song: Song)
    suspend fun getSongById(id: String): Song?
    fun getAllSongsStream(): Flow<List<SongEntity>>
    fun getFavSongsStream(): Flow<List<SongEntity>>
}
