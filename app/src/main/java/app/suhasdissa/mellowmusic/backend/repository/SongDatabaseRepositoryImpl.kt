package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import kotlinx.coroutines.flow.Flow

class SongDatabaseRepositoryImpl(private val songsDao: SongsDao) :
    SongDatabaseRepository {
    override suspend fun addSong(song: Song) = songsDao.addSong(song)
    override suspend fun getSongById(id: String): Song? = songsDao.getSongById(id)
    override fun getAllSongsStream(): Flow<List<Song>> = songsDao.getAllSongsStream()
    override fun getFavSongsStream(): Flow<List<Song>> = songsDao.getFavSongsStream()
    override suspend fun removeSong(song: Song) = songsDao.removeSong(song)
}
