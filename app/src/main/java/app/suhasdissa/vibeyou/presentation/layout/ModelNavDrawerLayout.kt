package app.suhasdissa.vibeyou.presentation.layout

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import app.suhasdissa.vibeyou.navigation.AppNavHost
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.components.MiniPlayerScaffold
import app.suhasdissa.vibeyou.presentation.components.ModalNavDrawerContent
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel
import kotlinx.coroutines.launch

@Composable
internal fun ModelNavDrawerLayout(
    navHostController: NavHostController,
    playerViewModel: PlayerViewModel,
    settingsModel: SettingsModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
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
                    navHostController.popBackStack()
                    navHostController.navigate(it)
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