package app.suhasdissa.mellowmusic.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.mellowmusic.About
import app.suhasdissa.mellowmusic.DatabaseSettings
import app.suhasdissa.mellowmusic.NetworkSettings
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.ui.components.SettingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigate: (route: String) -> Unit
) {
    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.settings_title)) }
        )
    }) { innerPadding ->
        LazyColumn(
            modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingItem(
                    title = stringResource(R.string.backup_restore),
                    description = stringResource(R.string.backup_restore_setting_description),
                    onClick = { onNavigate(DatabaseSettings.route) },
                    icon = Icons.Default.SettingsBackupRestore
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.network_settings),
                    description = stringResource(R.string.network_settings_description),
                    onClick = { onNavigate(NetworkSettings.route) },
                    icon = Icons.Default.Web
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.about_title),
                    description = stringResource(R.string.about_setting_description),
                    onClick = { onNavigate(About.route) },
                    icon = Icons.Outlined.Info
                )
            }
        }
    }
}
