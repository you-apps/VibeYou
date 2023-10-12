package app.suhasdissa.vibeyou

import android.content.ContentResolver
import androidx.media3.session.MediaController
import app.suhasdissa.vibeyou.backend.database.SongDatabase
import app.suhasdissa.vibeyou.backend.repository.AuthRepository
import app.suhasdissa.vibeyou.backend.repository.AuthRepositoryImpl
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import app.suhasdissa.vibeyou.backend.repository.PipedMusicRepository
import app.suhasdissa.vibeyou.backend.repository.SongDatabaseRepository
import app.suhasdissa.vibeyou.backend.repository.SongDatabaseRepositoryImpl
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
