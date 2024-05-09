package app.suhasdissa.vibeyou

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.presentation.layout.MainAppContent
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel
import app.suhasdissa.vibeyou.ui.theme.VibeYouTheme
import app.suhasdissa.vibeyou.utils.ThemeUtil

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels { PlayerViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsModel: SettingsModel = viewModel(factory = SettingsModel.Factory)
            val playerViewModel: PlayerViewModel =
                viewModel(factory = PlayerViewModel.Factory)

            val darkTheme = when (settingsModel.themeMode) {
                SettingsModel.Theme.SYSTEM -> isSystemInDarkTheme()
                SettingsModel.Theme.DARK, SettingsModel.Theme.AMOLED -> true
                else -> false
            }
            VibeYouTheme(
                darkTheme = darkTheme,
                customColorScheme = ThemeUtil.getSchemeFromSeed(
                    settingsModel.customColor,
                    darkTheme
                ),
                dynamicColor = settingsModel.colorTheme == SettingsModel.ColorTheme.SYSTEM,
                amoledDark = settingsModel.themeMode == SettingsModel.Theme.AMOLED
            ) {
                val primaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f).toArgb()

                LaunchedEffect(Unit) {
                    (application as MellowMusicApplication).accentColor = primaryColor
                }
                Surface {
                    MainAppContent(
                        playerViewModel, settingsModel
                    )
                }
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val uri = intent.data ?: return
                // Check if uri points to a device file
                if (uri.scheme == "file" || uri.scheme == "content") {
                    playerViewModel.tryToPlayUri(uri)
                } else {
                    processLink(uri)
                }
            }

            Intent.ACTION_SEND -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)
                    ?.let { sharedContent ->
                        intent.removeExtra(Intent.EXTRA_TEXT)
                        Log.e("IntentHandler", sharedContent)
                        val pattern = Regex("https://youtu.be/[\\S]+")
                        if (pattern.containsMatchIn(sharedContent)) {
                            kotlin.runCatching {
                                val uri = sharedContent.toUri()
                                processLink(uri)
                            }
                        }
                    }
            }
        }
    }

    private fun processLink(uri: Uri) {
        Toast.makeText(this, "Opening url...", Toast.LENGTH_SHORT).show()
        val path = uri.pathSegments.firstOrNull()

        val vidId = if (path == "watch") {
            uri.getQueryParameter("v")
        } else if (uri.host == "youtu.be") {
            path
        } else {
            null
        }

        if (vidId == null) {
            Toast.makeText(this, "Invalid Url", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, vidId, Toast.LENGTH_SHORT).show()
        playerViewModel.tryToPlayId(vidId)
    }
}