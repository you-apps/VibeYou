package app.suhasdissa.mellowmusic.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.Destination
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.models.Login
import app.suhasdissa.mellowmusic.backend.viewmodel.AuthViewModel
import app.suhasdissa.mellowmusic.ui.components.CacheSizeDialog
import app.suhasdissa.mellowmusic.ui.components.SettingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigate: (route: String) -> Unit
) {
    var showLoginDialog by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
    val topBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showImageCacheDialog by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(R.string.settings_title)) },
            scrollBehavior = topBarBehavior
        )
    }) { innerPadding ->
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(topBarBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingItem(
                    title = stringResource(R.string.backup_restore),
                    description = stringResource(R.string.backup_restore_setting_description),
                    onClick = { onNavigate(Destination.DatabaseSettings.route) },
                    icon = Icons.Default.SettingsBackupRestore
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.network_settings),
                    description = stringResource(R.string.network_settings_description),
                    onClick = { onNavigate(Destination.NetworkSettings.route) },
                    icon = Icons.Default.Web
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.music_cache_limit),
                    description = stringResource(R.string.change_music_cache_size),
                    onClick = {
                        showImageCacheDialog = true
                    },
                    icon = Icons.Default.Storage
                )
            }
            /*
            item {
                SettingItem(
                    title = stringResource(R.string.login_to_piped),
                    description = stringResource(R.string.login_to_piped_description),
                    onClick = { showLoginDialog = true },
                    icon = Icons.Default.AccountCircle
                )
            }
             */
            item {
                SettingItem(
                    title = stringResource(R.string.about_title),
                    description = stringResource(R.string.about_setting_description),
                    onClick = { onNavigate(Destination.About.route) },
                    icon = Icons.Outlined.Info
                )
            }
        }
    }
    if (showLoginDialog) {
        var username by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            confirmButton = {
                Button(onClick = { authViewModel.login(context, Login(username, pass)) }) {
                    Text(stringResource(R.string.login))
                }
            },
            text = {
                Column {
                    OutlinedTextField(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(stringResource(R.string.username)) }
                    )
                    OutlinedTextField(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text(stringResource(R.string.password)) }
                    )
                }
            }
        )
    }
    if (showImageCacheDialog) {
        CacheSizeDialog {
            showImageCacheDialog = false
        }
    }
}
