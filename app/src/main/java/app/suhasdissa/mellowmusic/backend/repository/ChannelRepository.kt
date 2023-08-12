package app.suhasdissa.mellowmusic.backend.repository

import android.util.Log
import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.models.artists.Channel
import app.suhasdissa.mellowmusic.backend.models.artists.ChannelTab
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist

interface ChannelRepository {
    suspend fun getChannelInfo(channelId: String): Channel
    suspend fun getChannelPlaylists(tabs: List<ChannelTab>): List<Playlist>?
}

class ChannelRepositoryImpl(private val pipedApi: PipedApi) : ChannelRepository {
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
}
