package app.suhasdissa.vibeyou.presentation.components

import android.view.SoundEffectConstants
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.QueuePlayNext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Song
import app.suhasdissa.vibeyou.presentation.screens.onlinemusic.model.SongOptionsViewModel
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongSettingsSheet(
    onDismissRequest: () -> Unit,
    song: Song,
    playerViewModel: PlayerViewModel,
    songViewModel: SongOptionsViewModel = viewModel(factory = SongOptionsViewModel.Factory),
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
                model = song.thumbnailUri,
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
                    song.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                song.artistsText?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (!song.isLocal) {
                Column(
                    Modifier
                        .padding(8.dp)
                ) {
                    var favouriteState by remember { mutableStateOf(song.isFavourite) }
                    IconButton(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        songViewModel.toggleFavourite(song.id)
                        favouriteState = !favouriteState
                    }) {
                        if (favouriteState) {
                            Icon(Icons.Rounded.Favorite, contentDescription = null)
                        } else {
                            Icon(Icons.Rounded.FavoriteBorder, contentDescription = null)
                        }
                    }
                }
            }
        }
        Column(Modifier.padding(vertical = 16.dp)) {
            SheetSettingItem(
                icon = Icons.Rounded.PlayArrow,
                description = R.string.play_song,
                onClick = {
                    playerViewModel.playSong(song)
                    onDismissRequest()
                }
            )
            SheetSettingItem(
                icon = Icons.Rounded.QueuePlayNext,
                description = R.string.play_next,
                onClick = {
                    playerViewModel.playNext(song)
                    onDismissRequest()
                }
            )
            SheetSettingItem(
                icon = Icons.AutoMirrored.Rounded.QueueMusic,
                description = R.string.enqueue_song,
                onClick = {
                    playerViewModel.enqueueSong(song)
                    onDismissRequest()
                }
            )
            if (!song.isLocal) {
                SheetSettingItem(
                    icon = Icons.Rounded.Delete,
                    description = R.string.delete_song,
                    onClick = {
                        songViewModel.removeSong(song)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongSettingsSheetSearchPage(
    onDismissRequest: () -> Unit,
    song: Song,
    playerViewModel: PlayerViewModel
) {
    val songSettingsSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
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
                model = song.thumbnailUri,
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
                    song.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                song.artistsText?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        Column(Modifier.padding(vertical = 16.dp)) {
            SheetSettingItem(
                icon = Icons.Rounded.PlayArrow,
                description = R.string.play_song,
                onClick = {
                    playerViewModel.playSong(song)
                    if (!song.isLocal) {
                        playerViewModel.saveSong(song)
                    }
                    onDismissRequest()
                }
            )
            SheetSettingItem(
                icon = Icons.Rounded.QueuePlayNext,
                description = R.string.play_next,
                onClick = {
                    playerViewModel.playNext(song)
                    if (!song.isLocal) {
                        playerViewModel.saveSong(song)
                    }
                    onDismissRequest()
                }
            )
            SheetSettingItem(
                icon = Icons.AutoMirrored.Rounded.QueueMusic,
                description = R.string.enqueue_song,
                onClick = {
                    playerViewModel.enqueueSong(song)
                    if (!song.isLocal) {
                        playerViewModel.saveSong(song)
                    }
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun SheetSettingItem(icon: ImageVector, @StringRes description: Int, onClick: () -> Unit) {
    val view = LocalView.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Text(text = stringResource(id = description))
    }
}
