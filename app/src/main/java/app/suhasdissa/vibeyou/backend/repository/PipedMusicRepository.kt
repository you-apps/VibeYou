package app.suhasdissa.vibeyou.backend.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.data.Artist
import app.suhasdissa.vibeyou.backend.data.Song
import app.suhasdissa.vibeyou.backend.database.dao.SearchDao
import app.suhasdissa.vibeyou.backend.database.dao.SongsDao
import app.suhasdissa.vibeyou.backend.database.entities.SearchQuery
import app.suhasdissa.vibeyou.backend.models.SearchFilter
import app.suhasdissa.vibeyou.backend.models.artists.Channel
import app.suhasdissa.vibeyou.backend.models.artists.ChannelTab
import app.suhasdissa.vibeyou.backend.models.playlists.PlaylistInfo
import app.suhasdissa.vibeyou.utils.RetrofitHelper
import app.suhasdissa.vibeyou.utils.asAlbum
import app.suhasdissa.vibeyou.utils.asArtist
import app.suhasdissa.vibeyou.utils.asSong
import app.suhasdissa.vibeyou.utils.asSongEntity

class PipedMusicRepository(
    private val songsDao: SongsDao,
    private val searchDao: SearchDao
) {
    var pipedApi = RetrofitHelper.createPipedApi()

    suspend fun getAudioSource(id: String): Uri? {
        return runCatching { pipedApi.getStreams(vidId = id) }
            .getOrNull()
            ?.audioStreams
            ?.get(1)
            ?.url
            ?.toUri()
    }
//
//    suspend fun getRecommendedSongs(id: String): List<MediaItem> {
//        val relatedSongs =
//            pipedApi.getStreams(vidId = id).relatedStreams.slice(0..1).map {
//                it.asSong
//            }
//        songsDao.addSongs(relatedSongs)
//        return relatedSongs.map { it.asMediaItem }
//    }

    suspend fun getPlaylistInfo(playlistId: String): PlaylistInfo =
        pipedApi.getPlaylistInfo(playlistId = playlistId)

    suspend fun getChannelInfo(channelId: String): Channel =
        pipedApi.getChannel(channelId = channelId)

    suspend fun getChannelPlaylists(channelId: String, tabs: List<ChannelTab>): List<Album> {
        val data = tabs.firstOrNull { it.name == "playlists" }?.data ?: return listOf()
        return try {
            pipedApi.getChannelTab(data = data).content.map { it.asAlbum }
        } catch (e: Exception) {
            Log.e("GetChannel Playlists", e.message, e)
            listOf()
        }
    }

    suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song> {
        if (filter != SearchFilter.Songs && filter != SearchFilter.Videos) {
            throw Exception(
                "Invalid filter for search"
            )
        }
        return pipedApi.searchPiped(query = query, filter = filter.value).items.map {
            it.asSong
        }
    }

    suspend fun getPlaylistResult(query: String, filter: SearchFilter): List<Album> {
        if (filter != SearchFilter.Playlists && filter != SearchFilter.Albums) {
            throw Exception(
                "Invalid filter for search"
            )
        }
        return pipedApi.searchPipedPlaylists(
            query = query,
            filter = filter.value
        ).items.map { it.asAlbum }
    }

    suspend fun getArtistResult(query: String): List<Artist> {
        return pipedApi.searchPipedArtists(query = query).items.map { it.asArtist }
    }

    suspend fun getSuggestions(query: String): List<String> {
        return pipedApi.getSuggestions(query = query)
    }

    suspend fun searchSongId(id: String): Song? {
        songsDao.getSongById(id)?.let {
            return it.asSong
        }
        try {
            val songResponse = pipedApi.getStreams(vidId = id)
            songResponse.title?.let {
                with(songResponse) {
                    val song = asSong(id)
                    songsDao.addSong(song.asSongEntity)
                    return song
                }
            } ?: return null
        } catch (_: Exception) {
            return null
        }
    }

    fun searchLocalSong(query: String): List<Song> =
        songsDao.search(query).map { it.asSong }

    fun saveSearchQuery(query: String) = searchDao.addSearchQuery(SearchQuery(id = 0, query))
    fun getSearchHistory() = searchDao.getSearchHistory()
}
