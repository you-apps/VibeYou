package app.suhasdissa.mellowmusic.ui.screens.home

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.suhasdissa.mellowmusic.Destination
import app.suhasdissa.mellowmusic.MainActivity
import app.suhasdissa.mellowmusic.MellowMusicApplication
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.repository.LocalMusicRepository
import app.suhasdissa.mellowmusic.navigateTo
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold
import app.suhasdissa.mellowmusic.ui.components.NavDrawerContent
import app.suhasdissa.mellowmusic.ui.screens.music.MusicScreen
import app.suhasdissa.mellowmusic.utils.PermissionHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (Destination) -> Unit,
    onSearch: (isOnline: Boolean) -> Unit
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    val mainActivity = (LocalContext.current as MainActivity)
    var currentDestination by remember {
        mutableStateOf<Destination>(Destination.PipedMusic)
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
            CenterAlignedTopAppBar(navigationIcon = {
                IconButton(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Navigation Drawer"
                    )
                }
            }, title = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clickable {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            onSearch(currentDestination == Destination.PipedMusic)
                        },
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null
                        )
                        Text(
                            stringResource(id = R.string.app_name),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            modifier = Modifier.padding(8.dp),
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                }
            })
        }) {
            NavHost(
                navController,
                startDestination = Destination.PipedMusic.route,
                Modifier
                    .fillMaxSize(),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                val container = (mainActivity.application as MellowMusicApplication).container
                composable(Destination.PipedMusic.route) {
                    LaunchedEffect(Unit) {
                        container.musicRepository = container.pipedMusicRepository
                    }
                    MusicScreen()
                }
                composable(Destination.LocalMusic.route) {
                    LaunchedEffect(Unit) {
                        container.musicRepository = container.localMusicRepository
                        PermissionHelper.checkPermissions(
                            mainActivity,
                            LocalMusicRepository.permissions
                        )
                    }
                    MusicScreen()
                }
            }
        }
    }
}
