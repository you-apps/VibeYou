package app.suhasdissa.vibeyou

import android.app.Application
import android.content.ComponentName
import android.graphics.Color
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import app.suhasdissa.vibeyou.data.database.SongDatabase
import app.suhasdissa.vibeyou.domain.models.primary.EqualizerData
import app.suhasdissa.vibeyou.utils.Pref
import app.suhasdissa.vibeyou.utils.UpdateUtil
import app.suhasdissa.vibeyou.utils.preferences
import app.suhasdissa.vibeyou.utils.services.PlayerService
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache

class MellowMusicApplication : Application(), ImageLoaderFactory {

    private val database by lazy { SongDatabase.getDatabase(this) }
    lateinit var container: AppContainer
    var accentColor: Int = Color.TRANSPARENT

    /**
     * Data stored here with the details of the equalizer
     */
    var supportedEqualizerData: EqualizerData? = null

    override fun onCreate() {
        super.onCreate()
        val sessionToken =
            SessionToken(
                this,
                ComponentName(this, PlayerService::class.java)
            )
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        container = DefaultAppContainer(database, controllerFuture, contentResolver)
        Pref.sharedPreferences = preferences
        UpdateUtil.getCurrentVersion(this.applicationContext)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .respectCacheHeaders(false)
            .diskCache(
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .build()
            ).build()
    }
}
