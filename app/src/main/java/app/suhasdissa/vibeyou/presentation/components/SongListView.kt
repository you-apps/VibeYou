package app.suhasdissa.vibeyou.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Song
import app.suhasdissa.vibeyou.presentation.screens.localmusic.components.SortOrder
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.utils.TimeUtil

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongListView(
    songs: List<Song>,
    playerViewModel: PlayerViewModel,
    sortOrder: SortOrder = SortOrder.Alphabetic
) {
    var showSongSettings by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    if (songs.isEmpty()) {
        IllustratedMessageScreen(image = R.drawable.ic_launcher_monochrome)
    } else {
        val groups = remember(songs) {
            when (sortOrder) {
                SortOrder.Alphabetic -> songs.groupBy { song -> song.title.first().toString() }
                SortOrder.Artist_Name -> songs.groupBy { song -> song.artistsText.orEmpty() }
                SortOrder.Creation_Date -> songs.groupBy { song ->
                    TimeUtil.getYear(
                        song.creationDate ?: 0
                    ).toString()
                }

                SortOrder.Date_Added -> songs.groupBy { song ->
                    TimeUtil.getYear(
                        song.dateAdded ?: 0
                    ).toString()
                }
            }
        }
        val state = rememberLazyListState()
//        LazyColumnScrollbar(
//            listState = state,
//            thumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//            thumbSelectedColor = MaterialTheme.colorScheme.primary,
//            thickness = 8.dp
//        ) {
        LazyColumn(
            Modifier.fillMaxSize(),
            state = state,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            groups.forEach { group ->
                stickyHeader {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Text(
                            text = group.key,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
                items(items = group.value) { item ->
                    SongCard(
                        song = item,
                        onClickCard = {
                            playerViewModel.playSong(item)
                        },
                        onLongPress = {
                            selectedSong = item
                            showSongSettings = true
                        }
                    )
                }
            }
        }
        //}
    }
    if (showSongSettings) {
        selectedSong?.let {
            SongSettingsSheet(
                onDismissRequest = { showSongSettings = false },
                song = selectedSong!!,
                playerViewModel
            )
        }
    }
}
