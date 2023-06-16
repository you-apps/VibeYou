package app.suhasdissa.libremusic.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.suhasdissa.libremusic.Player
import app.suhasdissa.libremusic.R
import app.suhasdissa.libremusic.Search
import app.suhasdissa.libremusic.Settings
import app.suhasdissa.libremusic.ui.player.MiniPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (route: String) -> Unit
) {
    val navController = rememberNavController()
    val items = listOf(
        HomeScreen.Songs,
        HomeScreen.FavouriteSongs
    )
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text(stringResource(R.string.app_name)) }, actions = {
            IconButton(onClick = { onNavigate(Settings.route) }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings_title)
                )
            }
            IconButton(onClick = { onNavigate(Player.route) }) {
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = stringResource(R.string.settings_title)
                )
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { onNavigate(Search.route) }) {
            Icon(
                imageVector = Icons.Filled.Search, stringResource(R.string.search)
            )
        }

    }, bottomBar = {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                NavigationBarItem(icon = screen.icon,
                    label = { Text(stringResource(screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }) { innerPadding ->
        Column {
            NavHost(
                navController,
                startDestination = HomeScreen.Songs.route,
                Modifier.padding(innerPadding)
            ) {
                composable(HomeScreen.Songs.route) {
                    SongsScreen(showFavourites = false)
                }
                composable(HomeScreen.FavouriteSongs.route) {
                    SongsScreen(showFavourites = true)
                }

            }

        }
        if (false) {
            MiniPlayer()
        }
    }
}

sealed class HomeScreen(
    val icon: @Composable () -> Unit, val route: String, @StringRes val resourceId: Int
) {
    object Songs : HomeScreen({
        Icon(
            imageVector = Icons.Default.MusicNote, contentDescription = null
        )
    }, "songs", R.string.songs)

    object FavouriteSongs : HomeScreen({
        Icon(
            imageVector = Icons.Default.Favorite, contentDescription = null
        )
    }, "fav_songs", R.string.favourite_songs)

}