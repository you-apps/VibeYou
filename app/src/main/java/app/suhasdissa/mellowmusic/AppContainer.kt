package app.suhasdissa.mellowmusic

import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.database.SongDatabase
import app.suhasdissa.mellowmusic.backend.repository.ChannelRepository
import app.suhasdissa.mellowmusic.backend.repository.ChannelRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.PlaylistRepository
import app.suhasdissa.mellowmusic.backend.repository.PlaylistRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.RadioRepository
import app.suhasdissa.mellowmusic.backend.repository.RadioRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.SearchRepository
import app.suhasdissa.mellowmusic.backend.repository.SearchRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.SongRepository
import app.suhasdissa.mellowmusic.backend.repository.SongRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val database: SongDatabase
    val searchRepository: SearchRepository
    val songRepository: SongRepository
    val radioRepository: RadioRepository
    val playlistRepository: PlaylistRepository
    val channelRepository: ChannelRepository
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

    override val searchRepository: SearchRepository by lazy {
        SearchRepositoryImpl(database.searchDao(), pipedApi)
    }
    override val songRepository: SongRepository by lazy {
        SongRepositoryImpl(database.songsDao(), pipedApi)
    }
    override val radioRepository: RadioRepository by lazy {
        RadioRepositoryImpl(database.songsDao(), pipedApi)
    }
    override val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepositoryImpl(pipedApi)
    }
    override val channelRepository: ChannelRepository by lazy {
        ChannelRepositoryImpl(pipedApi)
    }
}
