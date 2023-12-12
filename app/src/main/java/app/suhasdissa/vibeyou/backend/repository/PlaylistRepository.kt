package app.suhasdissa.vibeyou.backend.repository

import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.data.Song
import app.suhasdissa.vibeyou.backend.database.dao.PlaylistDao
import app.suhasdissa.vibeyou.backend.database.dao.SongsDao
import app.suhasdissa.vibeyou.backend.database.entities.PlaylistEntity
import app.suhasdissa.vibeyou.backend.database.entities.PlaylistWithSongs
import app.suhasdissa.vibeyou.backend.database.entities.SongPlaylistMap
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
}
