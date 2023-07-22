package app.suhasdissa.mellowmusic

import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.backend.database.SongDatabase
import app.suhasdissa.mellowmusic.backend.repository.RadioRepository
import app.suhasdissa.mellowmusic.backend.repository.RadioRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.SearchRepository
import app.suhasdissa.mellowmusic.backend.repository.SearchRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.SongRepository
import app.suhasdissa.mellowmusic.backend.repository.SongRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture

interface AppContainer {
    val database: SongDatabase
    val searchRepository: SearchRepository
    val songRepository: SongRepository
    val radioRepository: RadioRepository
    val controllerFuture: ListenableFuture<MediaController>
}

class DefaultAppContainer(
    override val database: SongDatabase,
    override val controllerFuture: ListenableFuture<MediaController>
) : AppContainer {

    override val searchRepository: SearchRepository by lazy {
        SearchRepositoryImpl(database.searchDao())
    }
    override val songRepository: SongRepository by lazy {
        SongRepositoryImpl(database.songsDao())
    }
    override val radioRepository: RadioRepository by lazy {
        RadioRepositoryImpl(database.songsDao())
    }
}
