package app.suhasdissa.vibeyou.presentation.screens.settings

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.models.Login
import app.suhasdissa.vibeyou.backend.viewmodel.AuthViewModel
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.screens.settings.components.CacheSizeDialog
import app.suhasdissa.vibeyou.presentation.screens.settings.components.SettingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Destination) -> Unit,
    onDrawerOpen: (() -> Unit)?,
) {
    var showLoginDialog by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
    val topBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var showImageCacheDialog by remember { mutableStateOf(false) }

    val view = LocalView.current
    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(R.string.settings_title)) },
            scrollBehavior = topBarBehavior,
            navigationIcon = {
                if (onDrawerOpen != null) {
                    IconButton(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        onDrawerOpen()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Open Navigation Drawer",
                        )
                    }
                }
            }
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
                    onClick = { onNavigate(Destination.DatabaseSettings) },
                    icon = Icons.Rounded.SettingsBackupRestore
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.network_settings),
                    description = stringResource(R.string.network_settings_description),
                    onClick = { onNavigate(Destination.NetworkSettings) },
                    icon = Icons.Rounded.Web
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.appearance_settings),
                    description = stringResource(R.string.appearance_settings_description),
                    onClick = { onNavigate(Destination.AppearanceSettings) },
                    icon = Icons.Rounded.Landscape
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.music_cache_limit),
                    description = stringResource(R.string.change_music_cache_size),
                    onClick = {
                        showImageCacheDialog = true
                    },
                    icon = Icons.Rounded.Storage
                )
            }
            /*
            item {
                SettingItem(
                    title = stringResource(R.string.login_to_piped),
                    description = stringResource(R.string.login_to_piped_description),
                    onClick = { showLoginDialog = true },
                    icon = Icons.Rounded.AccountCircle
                )
            }
             */
            item {
                SettingItem(
                    title = stringResource(R.string.about_title),
                    description = stringResource(R.string.about_setting_description),
                    onClick = { onNavigate(Destination.About) },
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
