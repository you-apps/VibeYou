package app.suhasdissa.mellowmusic.backend.repository

import android.net.Uri
import androidx.media3.common.MediaItem
import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.backend.database.entities.SearchQuery
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.SearchFilter
import app.suhasdissa.mellowmusic.backend.models.artists.Artist
import app.suhasdissa.mellowmusic.backend.models.artists.Channel
import app.suhasdissa.mellowmusic.backend.models.artists.ChannelTab
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist
import app.suhasdissa.mellowmusic.backend.models.playlists.PlaylistInfo

abstract class MusicRepository(
    private val searchDao: SearchDao,
    val songsDao: SongsDao,
) {
    abstract suspend fun getAudioSource(id: String): Uri?
    abstract suspend fun getRecommendedSongs(id: String): List<MediaItem>
    abstract suspend fun getSuggestions(query: String): List<String>
    abstract suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song>
    abstract suspend fun getPlaylistResult(query: String, filter: SearchFilter): List<Playlist>
    abstract suspend fun getArtistResult(query: String): List<Artist>
    abstract suspend fun getPlaylistInfo(playlistId: String): PlaylistInfo
    abstract suspend fun getChannelInfo(channelId: String): Channel
    abstract suspend fun getChannelPlaylists(channelId: String, tabs: List<ChannelTab>): List<Playlist>?
    abstract suspend fun searchSongId(id: String): Song?

    fun saveSearchQuery(query: String) = searchDao.addSearchQuery(SearchQuery(id = 0, query))
    fun getSearchHistory() = searchDao.getSearchHistory()
    fun searchLocalSong(query: String): List<Song> = songsDao.search(query)
}