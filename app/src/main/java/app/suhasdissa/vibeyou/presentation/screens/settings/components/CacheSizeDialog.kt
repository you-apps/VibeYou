package app.suhasdissa.vibeyou.presentation.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.utils.Pref
import app.suhasdissa.vibeyou.utils.formatMB
import app.suhasdissa.vibeyou.utils.rememberPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CacheSizeDialog(onDismissRequest: () -> Unit) {
    val cacheSizes = listOf(0, 512, 1024, 1024 * 2, 1024 * 4, 1024 * 6)
    var prefSize by rememberPreference(key = Pref.exoCacheKey, defaultValue = 0)
    AlertDialog(
        onDismissRequest,
        title = { Text(stringResource(R.string.change_music_cache_size)) },
        confirmButton = {
            Button(onClick = {
                onDismissRequest.invoke()
            }) {
                Text(text = stringResource(R.string.ok))
            }
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = cacheSizes) {
                    FilterChip(
                        selected = prefSize == it,
                        onClick = { prefSize = it },
                        label = {
                            Text(
                                if (it == 0) {
                                    stringResource(
                                        R.string.unlimited
                                    )
                                } else {
                                    formatMB(it)
                                }
                            )
                        }
                    )
                }
            }
        }
    )
}
