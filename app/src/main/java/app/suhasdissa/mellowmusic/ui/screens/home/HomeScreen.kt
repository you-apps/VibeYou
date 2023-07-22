package app.suhasdissa.mellowmusic.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.Destinations
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.Search
import app.suhasdissa.mellowmusic.Settings
import app.suhasdissa.mellowmusic.backend.viewmodel.SongViewModel
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (Destinations) -> Unit,
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        songViewModel.getRecentSongs()
    }
    MiniPlayerScaffold(fab = {}, topBar = {
        CenterAlignedTopAppBar(title = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable { onNavigate(Search) },
                shape = RoundedCornerShape(50)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null
                    )
                    Text(
                        stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
        }, actions = {
            IconButton(onClick = { onNavigate(Settings) }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            }
        })
    }) {
        HomeScreenView()
    }
}
