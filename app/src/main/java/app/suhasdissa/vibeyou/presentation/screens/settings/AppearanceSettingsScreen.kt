package app.suhasdissa.vibeyou.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.screens.settings.components.ButtonGroupPref
import app.suhasdissa.vibeyou.presentation.screens.settings.components.ColorPref
import app.suhasdissa.vibeyou.presentation.screens.settings.components.SwitchPref
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel
import app.suhasdissa.vibeyou.utils.Pref

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AppearanceSettingsScreen(settingsModel: SettingsModel) {
    val topBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(R.string.appearance_settings)) },
            scrollBehavior = topBarBehavior
        )
    }) { innerPadding ->
        val state = rememberScrollState()
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(topBarBehavior.nestedScrollConnection)
                .verticalScroll(state)
        ) {
            ButtonGroupPref(
                title = stringResource(R.string.theme),
                options = SettingsModel.Theme.values().map {
                    stringResource(it.resId)
                },
                values = SettingsModel.Theme.values().toList(),
                currentValue = settingsModel.themeMode
            ) {
                settingsModel.themeMode = it
            }
            ButtonGroupPref(
                title = stringResource(R.string.color_scheme),
                options = SettingsModel.ColorTheme.values().map {
                    stringResource(it.resId)
                },
                values = SettingsModel.ColorTheme.values().toList(),
                currentValue = settingsModel.colorTheme
            ) {
                settingsModel.colorTheme = it
            }
            AnimatedVisibility(
                visible = settingsModel.colorTheme == SettingsModel.ColorTheme.CATPPUCCIN
            ) {
                ColorPref(
                    selectedColor = settingsModel.customColor,
                    onSelect = {
                        settingsModel.customColor = it
                    }
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            SwitchPref(
                prefKey = Pref.thumbnailColorFallbackKey,
                title = stringResource(R.string.fallback_thumnail_accent),
                summary = stringResource(R.string.fallback_thumnail_accent_description)
            )
        }
    }
}
