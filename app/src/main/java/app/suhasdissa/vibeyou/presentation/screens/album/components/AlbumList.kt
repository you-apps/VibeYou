package app.suhasdissa.vibeyou.presentation.screens.album.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.presentation.components.IllustratedMessageScreen

@Composable
fun AlbumList(
    items: List<Album>,
    onClickCard: (playlist: Album) -> Unit,
    onLongPress: (playlist: Album) -> Unit
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
            AlbumCard(
                album = item,
                onClickCard = { onClickCard(item) },
                onLongPress = { onLongPress(item) }
            )
        }
    }
}
