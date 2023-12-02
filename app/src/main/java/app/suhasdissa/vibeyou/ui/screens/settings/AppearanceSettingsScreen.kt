package app.suhasdissa.vibeyou.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.utils.Pref

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettingsScreen() {
    val topBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(R.string.appearance_settings)) },
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
                SwitchPref(
                    prefKey = Pref.thumbnailColorFallbackKey,
                    title = stringResource(R.string.fallback_thumnail_accent),
                    summary = stringResource(R.string.fallback_thumnail_accent_description)
                )
            }
        }
    }
}
