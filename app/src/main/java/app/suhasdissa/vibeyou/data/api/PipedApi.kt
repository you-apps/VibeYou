package app.suhasdissa.vibeyou.data.api

import app.suhasdissa.vibeyou.backend.models.Login
import app.suhasdissa.vibeyou.backend.models.PipedInstance
import app.suhasdissa.vibeyou.backend.models.PipedSongResponse
import app.suhasdissa.vibeyou.backend.models.Token
import app.suhasdissa.vibeyou.backend.models.artists.Artists
import app.suhasdissa.vibeyou.backend.models.artists.Channel
import app.suhasdissa.vibeyou.backend.models.artists.ChannelTabResponse
import app.suhasdissa.vibeyou.backend.models.playlists.PlaylistInfo
import app.suhasdissa.vibeyou.backend.models.playlists.Playlists
import app.suhasdissa.vibeyou.backend.models.songs.Songs
import app.suhasdissa.vibeyou.utils.Pref
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PipedApi {
    /**
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     * @param filter This can only be "music_songs" or "music_videos"
     */
    @GET("https://{instance}/search")
    suspend fun searchPiped(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Query("q") query: String,
        @Query("filter") filter: String
    ): Songs

    /**
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     * @param filter This can only be "music_albums" or "music_playlists"
     */
    @GET("https://{instance}/search")
    suspend fun searchPipedPlaylists(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Query("q") query: String,
        @Query("filter") filter: String
    ): Playlists

    /**
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     */
    @GET("https://{instance}/search?filter=music_artists")
    suspend fun searchPipedArtists(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Query("q") query: String
    ): Artists

    /** Get stream details
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param vidId Youtube video id
     */
    @GET("https://{instance}/streams/{videoid}")
    suspend fun getStreams(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Path("videoid") vidId: String
    ): PipedSongResponse

    /** Get Search suggestions
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     */
    @GET("https://{instance}/suggestions")
    suspend fun getSuggestions(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Query("query") query: String
    ): List<String>

    /** Get Search suggestions
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param playlistId Playlist or Album Id
     */
    @GET("https://{instance}/playlists/{playlist_id}")
    suspend fun getPlaylistInfo(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Path("playlist_id") playlistId: String
    ): PlaylistInfo

    @POST("login")
    suspend fun login(@Body login: Login): Token

    @POST("register")
    suspend fun register(@Body login: Login): Token

    @GET("user/playlists")
    suspend fun getUserPlaylists(@Header("Authorization") token: String): List<PlaylistInfo>

    @GET("https://{instance}/channel/{channelId}")
    suspend fun getChannel(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Path("channelId") channelId: String
    ): Channel

    @GET("https://{instance}/channels/tabs")
    suspend fun getChannelTab(
        @Path("instance") instance: String = Pref.currentInstance.netLoc,
        @Query("data") data: String
    ): ChannelTabResponse

    @GET("https://piped-instances.kavin.rocks")
    suspend fun getInstanceList(): List<PipedInstance>
}
