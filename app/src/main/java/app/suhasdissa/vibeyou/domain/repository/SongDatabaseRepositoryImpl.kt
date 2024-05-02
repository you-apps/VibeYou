package app.suhasdissa.vibeyou.backend.repository

import app.suhasdissa.vibeyou.data.database.dao.SongsDao
import app.suhasdissa.vibeyou.data.database.entities.SongEntity
import app.suhasdissa.vibeyou.domain.models.primary.Song
import app.suhasdissa.vibeyou.utils.asSong
import app.suhasdissa.vibeyou.utils.asSongEntity
import kotlinx.coroutines.flow.Flow

class SongDatabaseRepositoryImpl(private val songsDao: SongsDao) :
    SongDatabaseRepository {
    override suspend fun addSong(song: Song) = songsDao.addSong(song.asSongEntity)
    override suspend fun addSongs(songs: List<Song>) =
        songsDao.addSongs(songs.map { it.asSongEntity })

    override suspend fun getSongById(id: String): Song? = songsDao.getSongById(id)?.asSong
    override fun getAllSongsStream(): Flow<List<SongEntity>> = songsDao.getAllSongsStream()
    override fun getFavSongsStream(): Flow<List<SongEntity>> = songsDao.getFavSongsStream()
    override suspend fun removeSong(song: Song) = songsDao.removeSong(song.asSongEntity)
}
