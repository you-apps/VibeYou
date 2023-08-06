package app.suhasdissa.mellowmusic.backend.api

import app.suhasdissa.mellowmusic.backend.models.PipedSearchResult
import app.suhasdissa.mellowmusic.backend.models.PipedSongAlbumResponse
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse
import app.suhasdissa.mellowmusic.utils.Pref
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val defaultHeader =
    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36" // ktlint-disable max-line-length

interface PipedApi {
    @Headers(defaultHeader)
    @GET("https://{instance}/search")
    suspend fun searchPiped(
        @Path("instance") instance: String = Pref.currentInstance,
        @Query("q") query: String,
        @Query("filter") filter: String
    ): PipedSearchResult

    @Headers(defaultHeader)
    @GET("https://{instance}/search?filter=music_albums")
    suspend fun searchPipedAlbums(
        @Path("instance") instance: String = Pref.currentInstance,
        @Query("q") query: String
    ): PipedSongAlbumResponse

    @Headers(defaultHeader)
    @GET("https://{instance}/streams/{videoid}")
    suspend fun getStreams(
        @Path("instance") instance: String = Pref.currentInstance,
        @Path("videoid") vidId: String
    ): PipedSongResponse

    @Headers(defaultHeader)
    @GET("https://{instance}/suggestions")
    suspend fun getSuggestions(
        @Path("instance") instance: String = Pref.currentInstance,
        @Query("query") query: String
    ): List<String>
}
