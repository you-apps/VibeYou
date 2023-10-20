package app.suhasdissa.vibeyou

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.suhasdissa.vibeyou.backend.viewmodel.LocalSearchViewModel
import app.suhasdissa.vibeyou.backend.viewmodel.LocalSongViewModel
import app.suhasdissa.vibeyou.backend.viewmodel.PipedSearchViewModel
import app.suhasdissa.vibeyou.ui.screens.home.HomeScreen
import app.suhasdissa.vibeyou.ui.screens.search.AlbumScreen
import app.suhasdissa.vibeyou.ui.screens.search.ArtistScreen
import app.suhasdissa.vibeyou.ui.screens.search.LocalSearchScreen
import app.suhasdissa.vibeyou.ui.screens.search.SearchScreen
import app.suhasdissa.vibeyou.ui.screens.settings.AboutScreen
import app.suhasdissa.vibeyou.ui.screens.settings.AppearanceSettingsScreen
import app.suhasdissa.vibeyou.ui.screens.settings.DatabaseSettingsScreen
import app.suhasdissa.vibeyou.ui.screens.settings.NetworkSettingsScreen
import app.suhasdissa.vibeyou.ui.screens.settings.SettingsScreen

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

        composable(route = Destination.AppearanceSettings.route) {
            AppearanceSettingsScreen()
        }

        composable(Destination.Playlists.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val searchViewModel: PipedSearchViewModel =
                    viewModel(factory = PipedSearchViewModel.Factory)
                AlbumScreen(searchViewModel.albumInfoState)
            }
        }

        composable(Destination.LocalPlaylists.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val searchViewModel: LocalSearchViewModel =
                    viewModel(factory = LocalSongViewModel.Factory)
                AlbumScreen(searchViewModel.albumInfoState)
            }
        }

        composable(route = Destination.Artist.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val searchViewModel: PipedSearchViewModel =
                    viewModel(factory = PipedSearchViewModel.Factory)
                ArtistScreen(onClickAlbum = {
                    searchViewModel.getPlaylistInfo(it)
                    navHostController.navigateTo(Destination.Playlists.route)
                }, searchViewModel.artistInfoState)
            }
        }

        composable(route = Destination.LocalArtist.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                val searchViewModel: LocalSearchViewModel =
                    viewModel(factory = LocalSearchViewModel.Factory)
                ArtistScreen(onClickAlbum = {
                    searchViewModel.getAlbumInfo(it)
                    navHostController.navigateTo(Destination.LocalPlaylists.route)
                }, searchViewModel.artistInfoState)
            }
        }
    }
}

fun NavHostController.navigateTo(route: String) = this.navigate(route) {
    launchSingleTop = true
    restoreState = true
}
