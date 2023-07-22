package app.suhasdissa.mellowmusic.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.ui.components.InstanceSelectDialog
import app.suhasdissa.mellowmusic.ui.components.SettingItem
import app.suhasdissa.mellowmusic.utils.Pref
import app.suhasdissa.mellowmusic.utils.rememberPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkSettingsScreen() {
    val currentServerId by rememberPreference(key = Pref.pipedInstanceKey, defaultValue = 0)
    var showDialog by remember { mutableStateOf(false) }
    var currentServer by remember {
        mutableStateOf(
            Pref.pipedInstances.getOrNull(currentServerId)?.name ?: Pref.pipedInstances.first().name
        )
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text(stringResource(R.string.network_settings)) })
    }) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                SettingItem(
                    title = stringResource(R.string.change_server),
                    description = currentServer,
                    icon = Icons.Default.Web
                ) {
                    showDialog = true
                }
            }
        }
    }
    if (showDialog) {
        InstanceSelectDialog(onDismissRequest = {
            showDialog = false
        }, onSelectionChange = { name ->
            currentServer = name
        })
    }
}
