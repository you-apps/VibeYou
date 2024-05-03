package app.suhasdissa.vibeyou.presentation.screens.home

import android.view.SoundEffectConstants
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.suhasdissa.vibeyou.MainActivity
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.navigation.HomeDestination
import app.suhasdissa.vibeyou.presentation.screens.localmusic.LocalMusicScreen
import app.suhasdissa.vibeyou.presentation.screens.onlinemusic.MusicScreen
import app.suhasdissa.vibeyou.utils.PermissionHelper

@Composable
fun HomeScreen(
    onNavigate: (Destination) -> Unit,
    onDrawerOpen: () -> Unit,
    navController: NavHostController
) {
    val view = LocalView.current
    val mainActivity = (LocalContext.current as MainActivity)

    var selectedRoute by remember {
        mutableStateOf<HomeDestination>(HomeDestination.LocalMusic)
    }

    // listen for destination changes (e.g. back presses)
    DisposableEffect(Unit) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            listOf(
                HomeDestination.LocalMusic,
                HomeDestination.OnlineMusic
            ).firstOrNull { destination.hasRoute(it::class) }
                ?.let { selectedRoute = it }
        }
        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    Scaffold(topBar = {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDrawerOpen()
            }) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Open Navigation Drawer"
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        if (selectedRoute == HomeDestination.OnlineMusic) {
                            onNavigate(Destination.OnlineSearch)
                        } else {
                            onNavigate(Destination.LocalSearch)
                        }
                    },
                shape = RoundedCornerShape(40)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                    Text(stringResource(R.string.search_songs))
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null
                    )
                }
            }
        }
    }) { pV ->
        val viewModelStoreOwner = LocalViewModelStoreOwner.current!!
        NavHost(
            navController,
            startDestination = HomeDestination.LocalMusic,
            Modifier
                .fillMaxSize()
                .padding(pV),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable<HomeDestination.OnlineMusic> {
                CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                    MusicScreen(onNavigate)
                }
            }
            composable<HomeDestination.LocalMusic> {
                LaunchedEffect(Unit) {
                    PermissionHelper.checkPermissions(
                        mainActivity,
                        LocalMusicRepository.permissions
                    )
                }
                LocalMusicScreen(onNavigate)
            }
        }
    }
}
