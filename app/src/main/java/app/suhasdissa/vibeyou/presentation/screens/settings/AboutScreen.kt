package app.suhasdissa.vibeyou.presentation.screens.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.screens.settings.components.SettingItem
import app.suhasdissa.vibeyou.presentation.screens.settings.model.CheckUpdateViewModel
import app.suhasdissa.vibeyou.utils.openBrowser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    updateViewModel: CheckUpdateViewModel = viewModel()
) {
    val context = LocalContext.current
    val githubRepo = "https://github.com/you-apps/VibeYou"
    val topBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(R.string.about_title)) },
            scrollBehavior = topBarBehavior
        )
    }) { innerPadding ->
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(topBarBehavior.nestedScrollConnection)
        ) {
            item {
                SettingItem(
                    title = stringResource(R.string.readme),
                    description = stringResource(R.string.check_repo_and_readme),
                    onClick = { openBrowser(context, githubRepo) },
                    icon = Icons.Rounded.Description
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.latest_release),
                    description = "${updateViewModel.latestVersion}",
                    onClick = {
                        openBrowser(
                            context,
                            "$githubRepo/releases/latest"
                        )
                    },
                    icon = Icons.Rounded.NewReleases
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.github_issue),
                    description = stringResource(R.string.github_issue_description),
                    onClick = {
                        openBrowser(
                            context,
                            "$githubRepo/issues"
                        )
                    },
                    icon = Icons.AutoMirrored.Rounded.ContactSupport
                )
            }
            item {
                SettingItem(
                    title = stringResource(R.string.current_version),
                    description = "${updateViewModel.currentVersion}",
                    onClick = {},
                    icon = Icons.Rounded.Info
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
