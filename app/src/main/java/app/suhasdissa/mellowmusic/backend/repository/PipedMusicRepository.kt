package app.suhasdissa.mellowmusic.backend.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.SearchFilter
import app.suhasdissa.mellowmusic.backend.models.artists.Artist
import app.suhasdissa.mellowmusic.backend.models.artists.Channel
import app.suhasdissa.mellowmusic.backend.models.artists.ChannelTab
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist
import app.suhasdissa.mellowmusic.backend.models.playlists.PlaylistInfo
import app.suhasdissa.mellowmusic.utils.asMediaItem
import app.suhasdissa.mellowmusic.utils.asSong

class PipedMusicRepository(
    private val pipedApi: PipedApi,
    private val songsDao: SongsDao,
    searchDao: SearchDao
): MusicRepository(searchDao, songsDao) {
    override suspend fun getAudioSource(id: String): Uri {
        return pipedApi.getStreams(vidId = id).audioStreams[1].url!!.toUri()
    }

    override suspend fun getRecommendedSongs(id: String): List<MediaItem> {
        val relatedSongs =
        pipedApi.getStreams(vidId = id).relatedStreams.slice(0..1).map {
            it.asSong
        }
        songsDao.addSongs(relatedSongs)
        return relatedSongs.map { it.asMediaItem }
    }

    override suspend fun getPlaylistInfo(playlistId: String): PlaylistInfo =
        pipedApi.getPlaylistInfo(playlistId = playlistId)

    override suspend fun getChannelInfo(channelId: String): Channel =
        pipedApi.getChannel(channelId = channelId)

    override suspend fun getChannelPlaylists(tabs: List<ChannelTab>): List<Playlist>? {
        val data = tabs.firstOrNull { it.name == "playlists" }?.data ?: return null
        return try {
            pipedApi.getChannelTab(data = data).content
        } catch (e: Exception) {
            Log.e("GetChannel Playlists", e.message, e)
            null
        }
    }

    override suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song> {
        if (filter != SearchFilter.Songs && filter != SearchFilter.Videos) {
            throw Exception(
                "Invalid filter for search"
            )
        }
        return pipedApi.searchPiped(query = query, filter = filter.value).items.map {
            it.asSong
        }
    }

    override suspend fun getPlaylistResult(query: String, filter: SearchFilter): List<Playlist> {
        if (filter != SearchFilter.Playlists && filter != SearchFilter.Albums) {
            throw Exception(
                "Invalid filter for search"
            )
        }
        return pipedApi.searchPipedPlaylists(query = query, filter = filter.value).items
    }

    override suspend fun getArtistResult(query: String): List<Artist> {
        return pipedApi.searchPipedArtists(query = query).items
    }

    override suspend fun getSuggestions(query: String): List<String> {
        return pipedApi.getSuggestions(query = query)
    }

    override suspend fun searchSongId(id: String): Song? {
        songsDao.getSongById(id)?.let {
            return it
        }
        try {
            val songResponse = pipedApi.getStreams(vidId = id)
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