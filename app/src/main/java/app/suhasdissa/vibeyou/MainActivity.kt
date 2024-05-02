package app.suhasdissa.vibeyou

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import app.suhasdissa.vibeyou.navigation.AppNavHost
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.components.NavDrawerContent
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel
import app.suhasdissa.vibeyou.ui.theme.VibeYouTheme
import app.suhasdissa.vibeyou.utils.ThemeUtil
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels { PlayerViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsModel: SettingsModel = viewModel(factory = SettingsModel.Factory)

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
                val navHostController = rememberNavController()
                val primaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f).toArgb()

                LaunchedEffect(Unit) {
                    (application as MellowMusicApplication).accentColor = primaryColor
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var currentDestination by remember {
                        mutableStateOf<Destination>(Destination.PipedMusic)
                    }
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        gesturesEnabled = drawerState.isOpen,
                        drawerContent = {
                            NavDrawerContent(
                                currentDestination = currentDestination,
                                onDestinationSelected = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    navHostController.navigate(it)
                                    currentDestination = it
                                }
                            )
                        }
                    ) {
                        AppNavHost(navHostController = navHostController)
                    }
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
