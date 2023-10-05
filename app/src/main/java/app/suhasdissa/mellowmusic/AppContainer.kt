package app.suhasdissa.mellowmusic

import android.content.ContentResolver
import androidx.media3.session.MediaController
import app.suhasdissa.mellowmusic.backend.database.SongDatabase
import app.suhasdissa.mellowmusic.backend.repository.AuthRepository
import app.suhasdissa.mellowmusic.backend.repository.AuthRepositoryImpl
import app.suhasdissa.mellowmusic.backend.repository.LocalMusicRepository
import app.suhasdissa.mellowmusic.backend.repository.PipedMusicRepository
import app.suhasdissa.mellowmusic.backend.repository.SongDatabaseRepository
import app.suhasdissa.mellowmusic.backend.repository.SongDatabaseRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture

interface AppContainer {
    val database: SongDatabase
    val songDatabaseRepository: SongDatabaseRepository
    val pipedMusicRepository: PipedMusicRepository
    val localMusicRepository: LocalMusicRepository
    val authRepository: AuthRepository
    val controllerFuture: ListenableFuture<MediaController>
    val contentResolver: ContentResolver
}

class DefaultAppContainer(
    override val database: SongDatabase,
    override val controllerFuture: ListenableFuture<MediaController>,
    override val contentResolver: ContentResolver
) : AppContainer {
    override val songDatabaseRepository: SongDatabaseRepository by lazy {
        SongDatabaseRepositoryImpl(database.songsDao())
    }
    override val pipedMusicRepository: PipedMusicRepository by lazy {
        PipedMusicRepository(database.songsDao(), database.searchDao())
    }
    override val localMusicRepository: LocalMusicRepository by lazy {
        LocalMusicRepository(contentResolver, database.searchDao())
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }
}
