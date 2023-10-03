package app.suhasdissa.mellowmusic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.suhasdissa.mellowmusic.backend.viewmodel.LocalSearchViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.LocalSongViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PipedSearchViewModel
import app.suhasdissa.mellowmusic.ui.screens.home.HomeScreen
import app.suhasdissa.mellowmusic.ui.screens.search.ArtistScreen
import app.suhasdissa.mellowmusic.ui.screens.search.LocalSearchScreen
import app.suhasdissa.mellowmusic.ui.screens.search.PlaylistScreen
import app.suhasdissa.mellowmusic.ui.screens.search.SearchScreen
import app.suhasdissa.mellowmusic.ui.screens.settings.AboutScreen
import app.suhasdissa.mellowmusic.ui.screens.settings.DatabaseSettingsScreen
import app.suhasdissa.mellowmusic.ui.screens.settings.NetworkSettingsScreen
import app.suhasdissa.mellowmusic.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(navHostController: NavHostController) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current!!
    NavHost(
        navController = navHostController,
        startDestination = Destination.PipedMusic.route
    ) {
        composable(route = Destination.PipedMusic.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                HomeScreen(onNavigate = { destination ->
                    navHostController.navigateTo(destination.route)
                })
            }
        }

        composable(
            route = Destination.OnlineSearch.route
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                SearchScreen(onNavigate = {
                    navHostController.navigateTo(it.route)
                })
            }
        }
        composable(
            route = Destination.LocalSearch.route
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                LocalSearchScreen(onNavigate = {
                    navHostController.navigateTo(it.route)
                })
            }
        }

        composable(route = Destination.Settings.route) {
            SettingsScreen(onNavigate = { route ->
                navHostController.navigateTo(route)
            })
        }

        composable(route = Destination.About.route) {
            AboutScreen()
        }

        composable(route = Destination.NetworkSettings.route) {
            NetworkSettingsScreen()
        }

        composable(route = Destination.DatabaseSettings.route) {
            DatabaseSettingsScreen()
        }

        composable(Destination.Playlists.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val searchViewModel: PipedSearchViewModel =
                    viewModel(factory = PipedSearchViewModel.Factory)
                PlaylistScreen(searchViewModel.playlistInfoState)
            }
        }

        composable(Destination.LocalPlaylists.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val searchViewModel: LocalSearchViewModel =
                    viewModel(factory = LocalSongViewModel.Factory)
                PlaylistScreen(searchViewModel.playlistInfoState)
            }
        }

        composable(route = Destination.Artist.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                ArtistScreen(onNavigate = {
                    navHostController.navigateTo(it.route)
                })
            }
        }
    }
}

fun NavHostController.navigateTo(route: String) = this.navigate(route) {
    launchSingleTop = true
    restoreState = true
}
