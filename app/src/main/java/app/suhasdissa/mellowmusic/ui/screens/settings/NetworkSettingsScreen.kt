package app.suhasdissa.mellowmusic.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.viewmodel.SettingsViewModel
import app.suhasdissa.mellowmusic.ui.components.InstanceSelectDialog
import app.suhasdissa.mellowmusic.ui.components.SettingItem
import app.suhasdissa.mellowmusic.utils.Pref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkSettingsScreen() {
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)

    var showDialog by remember { mutableStateOf(false) }
    var currentServer by remember {
        mutableStateOf(Pref.currentInstance)
    }
    val topBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(Unit) {
        viewModel.loadInstances()
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(R.string.network_settings)) },
            scrollBehavior = topBarBehavior
        )
    }) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(topBarBehavior.nestedScrollConnection)
        ) {
            item {
                SettingItem(
                    title = stringResource(R.string.change_server),
                    description = currentServer.name,
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
        }, onSelectionChange = { instance ->
            currentServer = instance
            Pref.setInstance(instance)
        })
    }
}
