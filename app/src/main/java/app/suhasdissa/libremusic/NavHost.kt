package app.suhasdissa.libremusic

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.suhasdissa.libremusic.ui.screens.home.HomeScreen
import app.suhasdissa.libremusic.ui.screens.settings.AboutScreen
import app.suhasdissa.libremusic.ui.screens.settings.SettingsScreen
import app.suhasdissa.libremusic.ui.search.SearchScreen

@Composable
fun AppNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Home.route
    ) {
        composable(route = Home.route) {
            HomeScreen(onNavigate = { route ->
                navHostController.navigateTo(route)
            })
        }

        composable(route = Search.route) {
            SearchScreen()
        }

        composable(route = Settings.route) {
            SettingsScreen(onNavigate = { route ->
                navHostController.navigateTo(route)
            })
        }

        composable(route = About.route) {
            AboutScreen()
        }
    }
}

fun NavHostController.navigateTo(route: String) = this.navigate(route) {
    launchSingleTop = true
    restoreState = true
}