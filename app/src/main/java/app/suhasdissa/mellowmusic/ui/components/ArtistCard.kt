package app.suhasdissa.mellowmusic.ui.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.data.Artist
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistCard(
    artist: Artist,
    onClickCard: () -> Unit,
    onLongPress: () -> Unit
) {
    val view = LocalView.current
    val haptic = LocalHapticFeedback.current
    Column(
        Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onClickCard()
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongPress()
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .size(148.dp)
                .aspectRatio(1f)
                .clip(CircleShape),
            model = artist.thumbnailUri,
            contentDescription = stringResource(R.string.artist_avatar),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.music_placeholder)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            artist.artistsText,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
    }
}
