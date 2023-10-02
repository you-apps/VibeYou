package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.entities.SearchQuery
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.SearchFilter
import app.suhasdissa.mellowmusic.backend.models.artists.Artist
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist

abstract class MusicRepository(
    private val searchDao: SearchDao
) {
    abstract suspend fun getSuggestions(query: String): List<String>
    abstract suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song>
    abstract suspend fun getPlaylistResult(query: String, filter: SearchFilter): List<Playlist>
    abstract suspend fun getArtistResult(query: String): List<Artist>
    fun saveSearchQuery(query: String) = searchDao.addSearchQuery(SearchQuery(id = 0, query))
    fun getSearchHistory() = searchDao.getSearchHistory()
    abstract suspend fun searchLocalSong(query: String, rawQuery: String): List<Song>
}
