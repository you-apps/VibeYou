package app.suhasdissa.vibeyou.presentation.screens.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.screens.settings.components.SettingItem
import app.suhasdissa.vibeyou.presentation.screens.settings.components.SwitchPref
import app.suhasdissa.vibeyou.presentation.screens.settings.model.DatabaseViewModel
import app.suhasdissa.vibeyou.utils.Pref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseSettingsScreen(
    databaseViewModel: DatabaseViewModel = viewModel(factory = DatabaseViewModel.Factory)
) {
    val topBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(R.string.backup_restore)) },
            scrollBehavior = topBarBehavior
        )
    }) { innerPadding ->
        val context = LocalContext.current
        val backupLauncher =
            rememberLauncherForActivityResult(
                ActivityResultContracts.CreateDocument("application/vnd.sqlite3")
            ) { uri ->
                if (uri == null) return@rememberLauncherForActivityResult

                databaseViewModel.backupDatabase(uri, context)
            }
        val restoreLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                if (uri == null) return@rememberLauncherForActivityResult

                databaseViewModel.restoreDatabase(uri, context)
            }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(topBarBehavior.nestedScrollConnection)
        ) {
            item {
                val scope = rememberCoroutineScope()

                SwitchPref(
                    prefKey = Pref.disableSearchHistoryKey,
                    title = stringResource(R.string.disable_search_history)
                ) { newValue ->
                    if (newValue) scope.launch(Dispatchers.IO) {
                        val app = ((context as Activity).application as MellowMusicApplication)
                        app.container.database.searchDao().deleteAll()
                    }
                }
            }

            item {
                SettingItem(
                    title = stringResource(R.string.backup),
                    description = stringResource(R.string.backup_description),
                    icon = Icons.Rounded.Backup
                ) {
                    @SuppressLint("SimpleDateFormat")
                    val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")

                    try {
                        backupLauncher.launch("mellowmusic_${dateFormat.format(Date())}.db")
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            context.getText(R.string.something_went_wrong),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            item {
                SettingItem(
                    title = stringResource(R.string.restore),
                    description = stringResource(R.string.restore_description),
                    icon = Icons.Rounded.Restore
                ) {
                    try {
                        restoreLauncher.launch(
                            arrayOf(
                                "application/vnd.sqlite3",
                                "application/x-sqlite3",
                                "application/octet-stream"
                            )
                        )
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            context.getText(R.string.something_went_wrong),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}
