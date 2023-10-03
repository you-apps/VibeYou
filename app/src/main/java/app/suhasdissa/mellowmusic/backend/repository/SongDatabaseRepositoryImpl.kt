package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.backend.database.entities.SongEntity
import app.suhasdissa.mellowmusic.utils.asSong
import app.suhasdissa.mellowmusic.utils.asSongEntity
import kotlinx.coroutines.flow.Flow

class SongDatabaseRepositoryImpl(private val songsDao: SongsDao) :
    SongDatabaseRepository {
    override suspend fun addSong(song: Song) = songsDao.addSong(song.asSongEntity)
    override suspend fun getSongById(id: String): Song? = songsDao.getSongById(id)?.asSong
    override fun getAllSongsStream(): Flow<List<SongEntity>> = songsDao.getAllSongsStream()
    override fun getFavSongsStream(): Flow<List<SongEntity>> = songsDao.getFavSongsStream()
    override suspend fun removeSong(song: Song) = songsDao.removeSong(song.asSongEntity)
}
