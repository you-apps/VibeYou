package app.suhasdissa.vibeyou.backend.repository

import app.suhasdissa.vibeyou.data.database.dao.PlaylistDao
import app.suhasdissa.vibeyou.data.database.dao.SongsDao
import app.suhasdissa.vibeyou.data.database.entities.PlaylistEntity
import app.suhasdissa.vibeyou.data.database.entities.PlaylistWithSongs
import app.suhasdissa.vibeyou.data.database.entities.SongPlaylistMap
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Song
import app.suhasdissa.vibeyou.utils.asPlaylistEntity
import app.suhasdissa.vibeyou.utils.asSongEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlaylistRepository(private val playlistDao: PlaylistDao, private val songsDao: SongsDao) {
    private fun createNew(album: Album) {
        playlistDao.addPlaylist(
            album.asPlaylistEntity
        )
    }

    private fun addSongsToPlaylist(playlistId: String, songs: List<Song>) {
        songsDao.addSongs(songs.map { it.asSongEntity })
        val songMap = songs.map { SongPlaylistMap(playlistId, it.id) }
        playlistDao.addPlaylistMaps(songMap)
    }

    suspend fun newPlaylistWithSongs(album: Album, songs: List<Song>) =
        withContext(Dispatchers.IO) {
            createNew(album)
            addSongsToPlaylist(album.id, songs)
        }

    fun getPlaylists(): Flow<List<PlaylistEntity>> = playlistDao.getAllPlaylists()

    suspend fun getPlaylist(id: String): PlaylistWithSongs =
        withContext(Dispatchers.IO) { return@withContext playlistDao.getPlaylist(id) }

    suspend fun deletePlaylist(album: Album) =
        withContext(Dispatchers.IO) { playlistDao.removePlaylist(album.asPlaylistEntity) }

    suspend fun clearPlaylist(album: Album) = withContext(Dispatchers.IO) {
        playlistDao.clearPlaylist(album.id)
    }

    suspend fun deletePlaylistAndSongs(album: Album) = withContext(Dispatchers.IO) {
        val songs = playlistDao.getPlaylist(album.id).songs
        playlistDao.removePlaylist(album.asPlaylistEntity)
        songsDao.removeSongs(songs)
    }
}
