package app.suhasdissa.vibeyou.presentation.screens.playlists.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.presentation.components.SheetSettingItem
import app.suhasdissa.vibeyou.presentation.screens.playlists.model.PlaylistViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistOptionsSheet(
    onDismissRequest: () -> Unit,
    album: Album,
    playlistViewModel: PlaylistViewModel
) {
    val songSettingsSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val view = LocalView.current
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = songSettingsSheetState,
        dragHandle = null,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                model = album.thumbnailUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.music_placeholder)
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    album.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    album.artistsText,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(Modifier.padding(vertical = 16.dp)) {
            SheetSettingItem(
                icon = Icons.Rounded.Delete,
                description = R.string.delete_playlist,
                onClick = {
                    playlistViewModel.deletePlaylist(album)
                    onDismissRequest()
                }
            )
            SheetSettingItem(
                icon = Icons.Rounded.ClearAll,
                description = R.string.clear_playlist,
                onClick = {
                    playlistViewModel.clearPlaylist(album)
                    onDismissRequest()
                }
            )
            SheetSettingItem(
                icon = Icons.Rounded.DeleteSweep,
                description = R.string.delete_playlist_and_songs,
                onClick = {
                    playlistViewModel.deletePlaylistAndSongs(album)
                    onDismissRequest()
                }
            )
        }
    }
}
