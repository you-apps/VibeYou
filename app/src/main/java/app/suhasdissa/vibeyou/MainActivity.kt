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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import app.suhasdissa.vibeyou.navigation.AppNavHost
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.navigation.HomeDestination
import app.suhasdissa.vibeyou.presentation.components.MiniPlayerScaffold
import app.suhasdissa.vibeyou.presentation.components.ModalNavDrawerContent
import app.suhasdissa.vibeyou.presentation.components.PermanentNavDrawerContent
import app.suhasdissa.vibeyou.presentation.screens.player.FullScreenPlayer
import app.suhasdissa.vibeyou.presentation.screens.player.components.EqualizerSheet
import app.suhasdissa.vibeyou.presentation.screens.player.components.QueueSheet
import app.suhasdissa.vibeyou.presentation.screens.player.components.SongOptionsSheet
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel
import app.suhasdissa.vibeyou.ui.theme.VibeYouTheme
import app.suhasdissa.vibeyou.utils.ThemeUtil
import app.suhasdissa.vibeyou.utils.mediaItemState
import kotlinx.coroutines.launch

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

@Composable
private fun MainAppContent(
    playerViewModel: PlayerViewModel,
    settingsModel: SettingsModel,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val navHostController = rememberNavController()
    val homeNavHostController = rememberNavController()
    if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT) {
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> {
                ModelNavDrawerLayout(
                    navHostController,
                    homeNavHostController,
                    playerViewModel,
                    settingsModel
                )
            }

            WindowWidthSizeClass.MEDIUM -> {
                PermanentNavDrawerLayout(
                    navHostController,
                    homeNavHostController,
                    playerViewModel,
                    settingsModel
                )
            }

            WindowWidthSizeClass.EXPANDED -> {
                PermanentNavDrawerWithPlayerLayout(
                    navHostController,
                    homeNavHostController,
                    playerViewModel,
                    settingsModel
                )
            }
        }
    } else {
        PermanentNavDrawerLayout(
            navHostController,
            homeNavHostController,
            playerViewModel,
            settingsModel,
            horizontalPlayer = true
        )
    }
}

@Composable
private fun ModelNavDrawerLayout(
    navHostController: NavHostController,
    homeNavHostController: NavHostController,
    playerViewModel: PlayerViewModel,
    settingsModel: SettingsModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentDestination by remember {
        mutableStateOf<HomeDestination>(HomeDestination.LocalMusic)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalNavDrawerContent(
                currentDestination = currentDestination,
                onDestinationSelected = {
                    scope.launch {
                        drawerState.close()
                    }
                    when (it) {
                        is Destination -> navHostController.navigate(it)
                        is HomeDestination -> {
                            currentDestination = it
                            homeNavHostController.popBackStack()
                            homeNavHostController.navigate(it)
                        }
                    }
                }
            )
        }
    ) {
        MiniPlayerScaffold(playerViewModel) { pV ->
            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(pV)
                    .padding(pV),
                navHostController = navHostController,
                homeNavHostController = homeNavHostController,
                onDrawerOpen = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                playerViewModel,
                settingsModel
            )
        }

    }
}

@Composable
private fun PermanentNavDrawerLayout(
    navHostController: NavHostController,
    homeNavHostController: NavHostController,
    playerViewModel: PlayerViewModel,
    settingsModel: SettingsModel,
    horizontalPlayer: Boolean = false
) {
    var currentDestination by remember {
        mutableStateOf<Any>(HomeDestination.LocalMusic)
    }

    PermanentNavigationDrawer(drawerContent = {
        PermanentNavDrawerContent(
            currentDestination = currentDestination,
            onDestinationSelected = {
                currentDestination = it
                when (it) {
                    is Destination -> navHostController.navigate(it)
                    is HomeDestination -> {
                        homeNavHostController.popBackStack()
                        homeNavHostController.navigate(it)
                    }
                }
            }
        )
    }) {
        MiniPlayerScaffold(playerViewModel, horizontalPlayer = horizontalPlayer) { pV ->
            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(pV)
                    .padding(pV),
                navHostController = navHostController,
                homeNavHostController = homeNavHostController,
                onDrawerOpen = null,
                playerViewModel,
                settingsModel
            )
        }
    }
}

@Composable
private fun PermanentNavDrawerWithPlayerLayout(
    navHostController: NavHostController,
    homeNavHostController: NavHostController,
    playerViewModel: PlayerViewModel,
    settingsModel: SettingsModel
) {
    var currentDestination by remember {
        mutableStateOf<Any>(HomeDestination.LocalMusic)
    }

    PermanentNavigationDrawer(drawerContent = {
        PermanentNavDrawerContent(
            currentDestination = currentDestination,
            onDestinationSelected = {
                currentDestination = it
                when (it) {
                    is Destination -> navHostController.navigate(it)
                    is HomeDestination -> {
                        homeNavHostController.popBackStack()
                        homeNavHostController.navigate(it)
                    }
                }
            }
        )
    }) {
        Row(Modifier.fillMaxSize()) {
            AppNavHost(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                navHostController = navHostController,
                homeNavHostController = homeNavHostController,
                onDrawerOpen = null,
                playerViewModel,
                settingsModel
            )

            playerViewModel.controller?.let { controller ->
                val mediaItem by controller.mediaItemState()
                AnimatedVisibility(visible = mediaItem != null) {
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .width(400.dp)
                    ) {
                        var showQueueSheet by remember { mutableStateOf(false) }
                        var showSongOptions by remember { mutableStateOf(false) }
                        var showEqualizerSheet by remember {
                            mutableStateOf(false)
                        }
                        FullScreenPlayer(
                            controller,
                            onCollapse = null,
                            playerViewModel,
                            onClickShowQueueSheet = { showQueueSheet = true },
                            onClickShowSongOptions = { showSongOptions = true }
                        )
                        if (showQueueSheet) QueueSheet(onDismissRequest = {
                            showQueueSheet = false
                        }, playerViewModel)
                        if (showSongOptions) SongOptionsSheet(
                            onDismissRequest = { showSongOptions = false },
                            playerViewModel,
                            onClickShowEqualizer = { showEqualizerSheet = true }
                        )
                        if (showEqualizerSheet) {
                            val app =
                                LocalContext.current.applicationContext as MellowMusicApplication
                            EqualizerSheet(
                                equalizerData = app.supportedEqualizerData!!,
                                onDismissRequest = { showEqualizerSheet = false },
                                playerViewModel = playerViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}