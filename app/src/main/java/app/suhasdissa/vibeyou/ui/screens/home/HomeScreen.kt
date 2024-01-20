package app.suhasdissa.vibeyou.ui.screens.home

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.suhasdissa.vibeyou.Destination
import app.suhasdissa.vibeyou.MainActivity
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.repository.LocalMusicRepository
import app.suhasdissa.vibeyou.backend.viewmodel.LocalSearchViewModel
import app.suhasdissa.vibeyou.navigateTo
import app.suhasdissa.vibeyou.ui.components.MiniPlayerScaffold
import app.suhasdissa.vibeyou.ui.components.NavDrawerContent
import app.suhasdissa.vibeyou.ui.screens.music.LocalMusicScreen
import app.suhasdissa.vibeyou.ui.screens.music.MusicScreen
import app.suhasdissa.vibeyou.utils.PermissionHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (Destination) -> Unit,
    localSearchViewModel: LocalSearchViewModel = viewModel(factory = LocalSearchViewModel.Factory)
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val mainActivity = (LocalContext.current as MainActivity)
    var currentDestination by remember {
        mutableStateOf<Destination>(Destination.LocalMusic)
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            NavDrawerContent(currentDestination = currentDestination, onDestinationSelected = {
                scope.launch {
                    drawerState.close()
                }
                if (it == Destination.Settings) {
                    onNavigate(it)
                } else {
                    navController.navigateTo(it.route)
                    currentDestination = it
                }
            })
        }
    ) {
        MiniPlayerScaffold(topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    scope.launch {
                        drawerState.open()
                    }
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
                            if (currentDestination == Destination.PipedMusic) {
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
        }) {
            val viewModelStoreOwner = LocalViewModelStoreOwner.current!!
            NavHost(
                navController,
                startDestination = Destination.LocalMusic.route,
                Modifier
                    .fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                composable(Destination.PipedMusic.route) {
                    CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                        MusicScreen(onNavigate)
                        LaunchedEffect(Unit) {
                            currentDestination = Destination.PipedMusic
                        }
                    }
                }
                composable(Destination.LocalMusic.route) {
                    LaunchedEffect(Unit) {
                        currentDestination = Destination.LocalMusic
                        PermissionHelper.checkPermissions(
                            mainActivity,
                            LocalMusicRepository.permissions
                        )
                    }
                    LocalMusicScreen(onNavigate, localSearchViewModel)
                }
            }
        }
    }
}
