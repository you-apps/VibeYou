package app.suhasdissa.vibeyou.presentation.screens.artist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import app.suhasdissa.vibeyou.presentation.components.IllustratedMessageScreen

@Composable
fun ArtistList(
    items: List<Artist>,
    onClickCard: (artist: Artist) -> Unit,
    onLongPress: (artist: Artist) -> Unit
) {
    if (items.isEmpty()) {
        IllustratedMessageScreen(image = R.drawable.ic_launcher_monochrome)
    }
    val state = rememberLazyListState()
//    LazyColumnScrollbar(
//        listState = state,
//        thumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//        thumbSelectedColor = MaterialTheme.colorScheme.primary,
//        thickness = 8.dp
//    ) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(items = items) { item ->
            ArtistCard(
                artist = item,
                onClickCard = { onClickCard(item) },
                onLongPress = { onLongPress(item) }
            )
        }
    }
//    }
}
