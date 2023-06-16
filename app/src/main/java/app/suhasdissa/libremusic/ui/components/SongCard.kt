package app.suhasdissa.libremusic.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun SongCard(
    thumbnail: String?,
    title: String,
    artist: String?,
    duration: String?,
    onClickVideoCard: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(Modifier.clickable { onClickVideoCard() }) {
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(thumbnail).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier
                    .weight(2f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                artist?.let {
                    Text(artist, style = MaterialTheme.typography.titleSmall)
                }
                duration?.let {
                    Text(
                        duration,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}