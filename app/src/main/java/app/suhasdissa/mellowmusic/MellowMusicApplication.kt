package app.suhasdissa.mellowmusic

import android.app.Application
import android.content.ComponentName
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import app.suhasdissa.mellowmusic.backend.database.SongDatabase
import app.suhasdissa.mellowmusic.backend.services.PlayerService
import app.suhasdissa.mellowmusic.utils.Pref
import app.suhasdissa.mellowmusic.utils.UpdateUtil
import app.suhasdissa.mellowmusic.utils.preferences

class MellowMusicApplication : Application() {

    private val database by lazy { SongDatabase.getDatabase(this) }
    lateinit var container: AppContainer
    override fun onCreate() {
        with(preferences) {
            Pref.pipedUrl = this.getInt(Pref.pipedInstanceKey, 0)
        }
        super.onCreate()
        val sessionToken =
            SessionToken(
                this,
                ComponentName(this, PlayerService::class.java)
            )
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        container = DefaultAppContainer(database, controllerFuture)
        UpdateUtil.getCurrentVersion(this.applicationContext)
    }
}