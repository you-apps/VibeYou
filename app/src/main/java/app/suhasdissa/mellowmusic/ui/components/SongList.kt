package app.suhasdissa.mellowmusic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.suhasdissa.mellowmusic.backend.database.entities.Song


@Composable
fun SongList(items: List<Song>, onClickVideoCard: (song: Song) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(items = items) { item ->
            SongCard(
                thumbnail = item.thumbnailUrl,
                title = item.title,
                artist = item.artistsText,
                duration = item.durationText,
                onClickVideoCard = {
                    onClickVideoCard(item)
                })
        }
    }
}