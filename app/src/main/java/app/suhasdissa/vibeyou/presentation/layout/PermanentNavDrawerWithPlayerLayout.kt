package app.suhasdissa.vibeyou.presentation.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.navigation.AppNavHost
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.components.PermanentNavDrawerContent
import app.suhasdissa.vibeyou.presentation.screens.player.FullScreenPlayer
import app.suhasdissa.vibeyou.presentation.screens.player.components.EqualizerSheet
import app.suhasdissa.vibeyou.presentation.screens.player.components.QueueSheet
import app.suhasdissa.vibeyou.presentation.screens.player.components.SongOptionsSheet
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel
import app.suhasdissa.vibeyou.utils.mediaItemState

@Composable
internal fun PermanentNavDrawerWithPlayerLayout(
    navHostController: NavHostController,
    playerViewModel: PlayerViewModel,
    settingsModel: SettingsModel
) {
    var currentDestination by remember {
        mutableStateOf<Destination>(Destination.LocalMusic)
    }

    DisposableEffect(Unit) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            listOf(
                Destination.LocalMusic,
                Destination.OnlineMusic,
                Destination.Settings
            ).firstOrNull { destination.hasRoute(it::class) }
                ?.let { currentDestination = it }
        }
        navHostController.addOnDestinationChangedListener(listener)

        onDispose {
            navHostController.removeOnDestinationChangedListener(listener)
        }
    }

    PermanentNavigationDrawer(drawerContent = {
        PermanentNavDrawerContent(
            currentDestination = currentDestination,
            onDestinationSelected = {
                navHostController.popBackStack()
                navHostController.navigate(it)
            }
        )
    }) {
        Row(Modifier.fillMaxSize()) {
            AppNavHost(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                navHostController = navHostController,
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