package app.suhasdissa.mellowmusic.backend.api

import app.suhasdissa.mellowmusic.backend.models.PipedSearchResult
import app.suhasdissa.mellowmusic.backend.models.PipedSongAlbumResponse
import app.suhasdissa.mellowmusic.backend.models.PipedSongResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val defaultHeader =
    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36" // ktlint-disable max-line-length

interface PipedApi {
    @Headers(defaultHeader)
    @GET("search")
    suspend fun searchPiped(
        @Query("q") query: String,
        @Query("filter") filter: String
    ): PipedSearchResult

    @Headers(defaultHeader)
    @GET("search?filter=music_albums")
    suspend fun searchPipedAlbums(
        @Query("q") query: String
    ): PipedSongAlbumResponse

    @Headers(defaultHeader)
    @GET("streams/{videoid}")
    suspend fun getStreams(
        @Path("videoid") vidId: String
    ): PipedSongResponse

    @Headers(defaultHeader)
    @GET("suggestions")
    suspend fun getSuggestions(
        @Query("query") query: String
    ): List<String>
}

fun getRetrofit(baseUrl: String): Retrofit {
    val json = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
