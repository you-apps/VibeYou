package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.utils.asSong

interface SongRepository {
    suspend fun addSong(song: Song)
    suspend fun removeSong(song: Song)
    suspend fun getSongById(id: String): Song?
    suspend fun getAllSongs(): List<Song>
    suspend fun getRecentSongs(limit: Int): List<Song>
    suspend fun getFavSongs(): List<Song>
    suspend fun searchSongId(id: String): Song?
}

class SongRepositoryImpl(private val songsDao: SongsDao) : SongRepository {
    override suspend fun addSong(song: Song) {
        songsDao.addSong(song)
    }

    override suspend fun getSongById(id: String): Song? {
        return songsDao.getSongById(id)
    }

    override suspend fun getAllSongs(): List<Song> {
        return songsDao.getAllSongs()
    }

    override suspend fun getRecentSongs(limit: Int): List<Song> {
        return songsDao.getRecentSongs(limit)
    }

    override suspend fun getFavSongs(): List<Song> {
        return songsDao.getFavSongs()
    }

    override suspend fun removeSong(song: Song) {
        return songsDao.removeSong(song)
    }

    override suspend fun searchSongId(id: String): Song? {
        songsDao.getSongById(id)?.let{
            return it
        }
        try {
            val songResponse = PipedApi.retrofitService.getStreams(id)
            songResponse.title?.let {
                with(songResponse) {
                    val song = asSong(id)
                    songsDao.addSong(song)
                    return song
                }
            } ?: return null
        } catch (_: Exception) {
            return null
        }
    }

}