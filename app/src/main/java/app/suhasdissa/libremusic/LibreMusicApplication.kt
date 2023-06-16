package app.suhasdissa.libremusic

import android.app.Application
import android.content.ComponentName
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import app.suhasdissa.libremusic.backend.database.SongDatabase
import app.suhasdissa.libremusic.backend.services.PlayerService

class LibreMusicApplication : Application() {

    private val database by lazy { SongDatabase.getDatabase(this) }
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()

        val sessionToken =
            SessionToken(
                this,
                ComponentName(this, PlayerService::class.java)
            )
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        container = DefaultAppContainer(database, controllerFuture)
    }
}