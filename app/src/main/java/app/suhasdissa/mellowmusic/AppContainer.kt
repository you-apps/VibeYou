package app.suhasdissa.mellowmusic

import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.database.SongDatabase
import app.suhasdissa.mellowmusic.backend.repository.AuthRepository
import app.suhasdissa.mellowmusic.backend.repository.AuthRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.MusicRepository
import app.suhasdissa.mellowmusic.backend.repository.PipedMusicRepository
import app.suhasdissa.mellowmusic.backend.repository.SongRepository
import app.suhasdissa.mellowmusic.backend.repository.SongRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val database: SongDatabase
    val songRepository: SongRepository
    val musicRepository: MusicRepository
    val authRepository: AuthRepository
    val controllerFuture: ListenableFuture<MediaController>
    val pipedApi: PipedApi
}

class DefaultAppContainer(
    override val database: SongDatabase,
    override val controllerFuture: ListenableFuture<MediaController>
) : AppContainer {

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://pipedapi.kavin.rocks/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    override val pipedApi: PipedApi = retrofit
        .create(PipedApi::class.java)

    override val songRepository: SongRepository by lazy {
        SongRepositoryImpl(database.songsDao())
    }
    override val musicRepository: MusicRepository by lazy {
        PipedMusicRepository(pipedApi, database.songsDao(), database.searchDao())
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(pipedApi)
    }
}
