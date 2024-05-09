package app.suhasdissa.vibeyou.presentation.layout

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel

@Composable
fun MainAppContent(
    playerViewModel: PlayerViewModel,
    settingsModel: SettingsModel,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {
    val navHostController = rememberNavController()
    if (windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT) {
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> {
                ModelNavDrawerLayout(
                    navHostController,
                    playerViewModel,
                    settingsModel
                )
            }

            WindowWidthSizeClass.MEDIUM -> {
                PermanentNavDrawerLayout(
                    navHostController,
                    playerViewModel,
                    settingsModel
                )
            }

            WindowWidthSizeClass.EXPANDED -> {
                PermanentNavDrawerWithPlayerLayout(
                    navHostController,
                    playerViewModel,
                    settingsModel
                )
            }
        }
    } else {
        PermanentNavDrawerLayout(
            navHostController,
            playerViewModel,
            settingsModel,
            horizontalPlayer = true
        )
    }
}