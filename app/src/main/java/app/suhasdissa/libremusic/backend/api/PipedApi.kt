package app.suhasdissa.libremusic.backend.api

import app.suhasdissa.libremusic.backend.models.PipedSearchResult
import app.suhasdissa.libremusic.backend.models.PipedSongAlbumResponse
import app.suhasdissa.libremusic.backend.models.PipedSongResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private val json = Json { ignoreUnknownKeys = true }
private const val pipedUrl = "https://watchapi.whatever.social/"

private val retrofit = Retrofit.Builder()
    .baseUrl(pipedUrl)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()
private const val defaultHeader =
    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36"

interface ApiService {
    @Headers(defaultHeader)
    @GET("search?filter=music_songs")
    suspend fun searchPiped(
        @Query("q") query: String
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

object PipedApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}