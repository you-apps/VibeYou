package app.suhasdissa.mellowmusic.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.ui.components.SettingItem
import app.suhasdissa.mellowmusic.utils.openBrowser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val githubRepo = "https://github.com/SuhasDissa/LibreMusic"

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(title = { Text(stringResource(R.string.about_title)) })
    }) { innerPadding ->
        LazyColumn(
            modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                SettingItem(
                    title = stringResource(R.string.readme),
                    description = stringResource(R.string.check_repo_and_readme),
                    onClick = { openBrowser(context, githubRepo) },
                    icon = Icons.Default.Description
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.latest_release),
                    description = "",
                    onClick = {
                        openBrowser(
                            context, "$githubRepo/releases/latest"
                        )
                    },
                    icon = Icons.Default.NewReleases
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.github_issue),
                    description = stringResource(R.string.github_issue_description),
                    onClick = {
                        openBrowser(
                            context, "$githubRepo/issues"
                        )
                    },
                    icon = Icons.Default.ContactSupport
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.current_version),
                    description = "",
                    onClick = {},
                    icon = Icons.Default.Info
                )
            }

        }
    }
}

@Composable
@Preview
fun AboutScreenPreview() {
    AboutScreen()
}