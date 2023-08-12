package app.suhasdissa.mellowmusic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.suhasdissa.mellowmusic.ui.screens.home.HomeScreen
import app.suhasdissa.mellowmusic.ui.screens.search.ArtistScreen
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
        startDestination = Home.route
    ) {
        composable(route = Home.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                HomeScreen(onNavigate = { destination ->
                    navHostController.navigateTo(destination.route)
                })
            }
        }

        composable(route = Search.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                SearchScreen(onNavigate = {
                    navHostController.navigateTo(it.route)
                })
            }
        }

        composable(route = Settings.route) {
            SettingsScreen(onNavigate = { route ->
                navHostController.navigateTo(route)
            })
        }

        composable(route = About.route) {
            AboutScreen()
        }

        composable(route = NetworkSettings.route) {
            NetworkSettingsScreen()
        }

        composable(route = DatabaseSettings.route) {
            DatabaseSettingsScreen()
        }

        composable(Playlists.route) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                PlaylistScreen()
            }
        }

        composable(route = Artist.route) {
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
