package app.suhasdissa.mellowmusic.backend.api

import app.suhasdissa.mellowmusic.backend.models.Login
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse
import app.suhasdissa.mellowmusic.backend.models.Token
import app.suhasdissa.mellowmusic.backend.models.artists.Artists
import app.suhasdissa.mellowmusic.backend.models.artists.Channel
import app.suhasdissa.mellowmusic.backend.models.artists.ChannelTabResponse
import app.suhasdissa.mellowmusic.backend.models.playlists.PlaylistInfo
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlists
import app.suhasdissa.mellowmusic.backend.models.songs.SongItem
import app.suhasdissa.mellowmusic.backend.models.songs.Songs
import app.suhasdissa.mellowmusic.utils.DownloaderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.NewPipe

class NewPipeApi : PipedApi {
    private val youtubeService = NewPipe.getService(0)

    init {
        NewPipe.init(DownloaderImpl())
    }

    override suspend fun searchPiped(instance: String, query: String, filter: String): Songs {
        return withContext(Dispatchers.IO) {
            val searchExtractor = youtubeService.getSearchExtractor(query)
            val items = searchExtractor.initialPage.items.map {
                SongItem(
                    url = it.url,
                    title = it.name,
                    thumbnail = it.thumbnailUrl
                )
            }
            Songs(items = ArrayList(items))
        }
    }

    override suspend fun searchPipedPlaylists(
        instance: String,
        query: String,
        filter: String
    ): Playlists {
        TODO("Not yet implemented")
    }

    override suspend fun searchPipedArtists(instance: String, query: String): Artists {
        TODO("Not yet implemented")
    }

    override suspend fun getStreams(instance: String, vidId: String): PipedSongResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSuggestions(instance: String, query: String): List<String> {
        return withContext(Dispatchers.IO) {
            youtubeService.suggestionExtractor.suggestionList(query)
        }
    }

    override suspend fun getPlaylistInfo(instance: String, playlistId: String): PlaylistInfo {
        TODO("Not yet implemented")
    }

    override suspend fun login(login: Login): Token {
        TODO("Not yet implemented")
    }

    override suspend fun register(login: Login): Token {
        TODO("Not yet implemented")
    }

    override suspend fun getUserPlaylists(token: String): List<PlaylistInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannel(instance: String, channelId: String): Channel {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelTab(instance: String, data: String): ChannelTabResponse {
        TODO("Not yet implemented")
    }
}
