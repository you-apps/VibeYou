package app.suhasdissa.libremusic

import androidx.media3.session.MediaController
import app.suhasdissa.libremusic.backend.database.SongDatabase
import app.suhasdissa.libremusic.backend.repository.SearchRepository
import app.suhasdissa.libremusic.backend.repository.SearchRepositoryImpl
import app.suhasdissa.libremusic.backend.repository.SongRepository
import app.suhasdissa.libremusic.backend.repository.SongRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture

interface AppContainer {
    val searchRepository: SearchRepository
    val songRepository: SongRepository
    val controllerFuture: ListenableFuture<MediaController>
}

class DefaultAppContainer(
    database: SongDatabase,
    override val controllerFuture: ListenableFuture<MediaController>
) : AppContainer {

    override val searchRepository: SearchRepository by lazy {
        SearchRepositoryImpl(database.searchDao())
    }
    override val songRepository: SongRepository by lazy {
        SongRepositoryImpl(database.songsDao())
    }
}