package app.suhasdissa.vibeyou.presentation.screens.settings.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.edit
import androidx.documentfile.provider.DocumentFile
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.utils.Pref
import app.suhasdissa.vibeyou.utils.rememberPreference

@Composable
fun LocalMusicPathsDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current

    var allAvailableMusic by rememberPreference(key = Pref.showAllMusicKey, defaultValue = true)
    val musicPaths = remember {
        Pref.sharedPreferences.getStringSet(Pref.musicDirectoriesKey, setOf()).orEmpty().toMutableStateList()
    }

    val chooseFolderLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { uri ->
        val file = DocumentFile.fromTreeUri(context, uri ?: return@rememberLauncherForActivityResult) ?: return@rememberLauncherForActivityResult
        val relativePath = file.uri.path?.split(":")?.last().toString()
        musicPaths.add(relativePath)
        Pref.sharedPreferences.edit {
            putStringSet(Pref.musicDirectoriesKey, musicPaths.toSet())
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = R.string.local_music_paths))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.all_available_music)
                    )
                    Switch(checked = allAvailableMusic, onCheckedChange = {
                        allAvailableMusic = it
                    })
                }

                AnimatedVisibility(visible = !allAvailableMusic) {
                    LazyColumn {
                        items(musicPaths.toList()) { path ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = path
                                )
                                IconButton(
                                    onClick = {
                                        musicPaths.remove(path)
                                        Pref.sharedPreferences.edit {
                                            putStringSet(Pref.musicDirectoriesKey, musicPaths.toSet())
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = null)
                                }
                            }
                        }

                        item {
                            Button(onClick = { chooseFolderLauncher.launch(null) }) {
                                Text(text = stringResource(id = R.string.add_folder))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}