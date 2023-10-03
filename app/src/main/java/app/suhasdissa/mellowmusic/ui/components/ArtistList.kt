package app.suhasdissa.mellowmusic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.data.Artist

@Composable
fun ArtistList(
    items: List<Artist>,
    onClickCard: (artist: Artist) -> Unit,
    onLongPress: (artist: Artist) -> Unit
) {
    if (items.isEmpty()) {
        IllustratedMessageScreen(image = R.drawable.ic_launcher_monochrome)
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(items = items) { item ->
            ArtistCard(
                thumbnail = item.thumbnailUrl,
                title = item.artistsText,
                onClickCard = { onClickCard(item) },
                onLongPress = { onLongPress(item) }
            )
        }
    }
}
