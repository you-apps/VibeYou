package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.models.playlists.PlaylistInfo

interface PlaylistRepository {
    suspend fun getPlaylistInfo(playlistId: String): PlaylistInfo
}

class PlaylistRepositoryImpl(private val pipedApi: PipedApi) : PlaylistRepository {
    override suspend fun getPlaylistInfo(playlistId: String): PlaylistInfo =
        pipedApi.getPlaylistInfo(playlistId = playlistId)
}
