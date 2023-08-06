package app.suhasdissa.mellowmusic.backend.api

import app.suhasdissa.mellowmusic.backend.models.PipedSearchResult
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse
import app.suhasdissa.mellowmusic.backend.models.artists.Artists
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlists
import app.suhasdissa.mellowmusic.utils.Pref
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val defaultHeader =
    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36" // ktlint-disable max-line-length

interface PipedApi {
    /**
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     * @param filter This can only be "music_songs" or "music_videos"
     */
    @Headers(defaultHeader)
    @GET("https://{instance}/search")
    suspend fun searchPiped(
        @Path("instance") instance: String = Pref.currentInstance,
        @Query("q") query: String,
        @Query("filter") filter: String
    ): PipedSearchResult

    /**
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     * @param filter This can only be "music_albums" or "music_playlists"
     */
    @Headers(defaultHeader)
    @GET("https://{instance}/search")
    suspend fun searchPipedPlaylists(
        @Path("instance") instance: String = Pref.currentInstance,
        @Query("q") query: String,
        @Query("filter") filter: String
    ): Playlists

    /**
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     */
    @Headers(defaultHeader)
    @GET("https://{instance}/search?filter=music_artists")
    suspend fun searchPipedArtists(
        @Path("instance") instance: String = Pref.currentInstance,
        @Query("q") query: String
    ): Artists

    /** Get stream details
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param vidId Youtube video id
     */
    @Headers(defaultHeader)
    @GET("https://{instance}/streams/{videoid}")
    suspend fun getStreams(
        @Path("instance") instance: String = Pref.currentInstance,
        @Path("videoid") vidId: String
    ): PipedSongResponse

    /** Get Search suggestions
     * @param instance Piped instance ex: pipedapi.kavin.rocks
     * @param query Search terms without url encoding
     */
    @Headers(defaultHeader)
    @GET("https://{instance}/suggestions")
    suspend fun getSuggestions(
        @Path("instance") instance: String = Pref.currentInstance,
        @Query("query") query: String
    ): List<String>
}
