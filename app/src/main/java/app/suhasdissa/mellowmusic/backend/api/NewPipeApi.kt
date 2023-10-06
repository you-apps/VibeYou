package app.suhasdissa.mellowmusic.backend.api

import android.util.Log
import app.suhasdissa.mellowmusic.backend.models.AudioStreams
import app.suhasdissa.mellowmusic.backend.models.Login
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse
import app.suhasdissa.mellowmusic.backend.models.Token
import app.suhasdissa.mellowmusic.backend.models.artists.Artist
import app.suhasdissa.mellowmusic.backend.models.artists.Artists
import app.suhasdissa.mellowmusic.backend.models.artists.Channel
import app.suhasdissa.mellowmusic.backend.models.artists.ChannelTabResponse
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist
import app.suhasdissa.mellowmusic.backend.models.playlists.PlaylistInfo
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlists
import app.suhasdissa.mellowmusic.backend.models.songs.SongItem
import app.suhasdissa.mellowmusic.backend.models.songs.Songs
import app.suhasdissa.mellowmusic.utils.DownloaderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.search.SearchInfo
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem

class NewPipeApi : PipedApi {
    private val youtubeService = NewPipe.getService(0)

    init {
        NewPipe.init(DownloaderImpl())
        YoutubeStreamExtractor.forceFetchAndroidClient(true)
    }

    override suspend fun searchPiped(instance: String, query: String, filter: String): Songs {
        return withContext(Dispatchers.IO) {
            val info = SearchInfo.getInfo(
                youtubeService,
                youtubeService.searchQHFactory.fromQuery(query, listOf(filter), null)
            )
            val items = info.relatedItems
                .filterIsInstance(StreamInfoItem::class.java)
                .map { item: StreamInfoItem ->
                    SongItem(
                        url = item.url,
                        title = item.name,
                        thumbnail = item.thumbnailUrl,
                        uploaderName = item.uploaderName,
                        duration = item.duration
                    )
                }
            Songs(items = items)
        }
    }

    override suspend fun searchPipedPlaylists(
        instance: String,
        query: String,
        filter: String
    ): Playlists {
        return withContext(Dispatchers.IO) {
            val info = SearchInfo.getInfo(
                youtubeService,
                youtubeService.searchQHFactory.fromQuery(query, listOf(filter), null)
            )
            val items = info.relatedItems
                .filterIsInstance(PlaylistInfoItem::class.java)
                .map { item: PlaylistInfoItem ->
                    Playlist(
                        url = item.url,
                        type = item.playlistType.name,
                        name = item.name,
                        thumbnail = item.thumbnailUrl,
                        uploaderName = item.uploaderName
                    )
                }
            Playlists(items = items)
        }
    }

    override suspend fun searchPipedArtists(instance: String, query: String): Artists {
        return withContext(Dispatchers.IO) {
            val info = SearchInfo.getInfo(
                youtubeService,
                youtubeService.searchQHFactory.fromQuery(query, listOf(), null)
            )
            val items = info.relatedItems
                .filterIsInstance(ChannelInfoItem::class.java)
                .map { item: ChannelInfoItem ->
                    Artist(
                        url = item.url,
                        name = item.name,
                        thumbnail = item.thumbnailUrl,
                        description = item.description
                    )
                }
            Artists(items = items)
        }
    }

    override suspend fun getStreams(instance: String, vidId: String): PipedSongResponse {
        return withContext(Dispatchers.IO) {
            try {
                val resp =
                    StreamInfo.getInfo("https://www.youtube.com/watch?v=$vidId")
                val audioStreams = resp.audioStreams.map {
                    AudioStreams(it.url)
                }
                PipedSongResponse(
                    resp.name,
                    resp.description.toString(),
                    resp.uploaderName,
                    resp.thumbnailUrl,
                    resp.hlsUrl,
                    resp.duration,
                    audioStreams
                )
            } catch (e: Exception) {
                Log.e("getStreams", e.message, e)
                throw e
            }
        }
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
