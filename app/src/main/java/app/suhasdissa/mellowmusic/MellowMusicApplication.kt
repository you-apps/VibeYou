package app.suhasdissa.mellowmusic

import android.app.Application
import android.content.ComponentName
import android.content.SharedPreferences
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.api.getRetrofit
import app.suhasdissa.mellowmusic.backend.database.SongDatabase
import app.suhasdissa.mellowmusic.backend.services.PlayerService
import app.suhasdissa.mellowmusic.utils.Pref
import app.suhasdissa.mellowmusic.utils.UpdateUtil
import app.suhasdissa.mellowmusic.utils.preferences

class MellowMusicApplication : Application() {

    private val database by lazy { SongDatabase.getDatabase(this) }
    lateinit var container: AppContainer

    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == Pref.pipedInstanceKey) {
                Pref.pipedUrl = sharedPreferences.getInt(Pref.pipedInstanceKey, 0)
                container.pipedApi =
                    getRetrofit(Pref.currentInstanceUrl).create(PipedApi::class.java)
            }
        }

    override fun onCreate() {
        with(preferences) {
            Pref.pipedUrl = getInt(Pref.pipedInstanceKey, 0)
        }
        super.onCreate()
        val sessionToken =
            SessionToken(
                this,
                ComponentName(this, PlayerService::class.java)
            )
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        container = DefaultAppContainer(database, controllerFuture)
        preferences.registerOnSharedPreferenceChangeListener(listener)
        UpdateUtil.getCurrentVersion(this.applicationContext)
    }

    override fun onTerminate() {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
        super.onTerminate()
    }
}
