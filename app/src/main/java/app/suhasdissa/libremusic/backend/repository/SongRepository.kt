package app.suhasdissa.libremusic.backend.repository

import app.suhasdissa.libremusic.backend.database.dao.SongsDao
import app.suhasdissa.libremusic.backend.database.entities.Song

interface SongRepository {
    suspend fun addSong(song: Song)
    suspend fun getAllSongs(): List<Song>
    suspend fun getFavSongs(): List<Song>
}

class SongRepositoryImpl(private val songsDao: SongsDao) : SongRepository {
    override suspend fun addSong(song: Song) {
        songsDao.addSong(song)
    }

    override suspend fun getAllSongs(): List<Song> {
        return songsDao.getAllSongs()
    }

    override suspend fun getFavSongs(): List<Song> {
        return songsDao.getFavSongs()
    }

}