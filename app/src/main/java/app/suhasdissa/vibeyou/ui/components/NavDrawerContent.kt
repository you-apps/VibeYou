package app.suhasdissa.vibeyou.ui.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.Destination
import app.suhasdissa.vibeyou.R

@Composable
fun NavDrawerContent(
    currentDestination: Destination,
    onDestinationSelected: (Destination) -> Unit
) {
    val view = LocalView.current
    ModalDrawerSheet(modifier = Modifier.width(250.dp)) {
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(128.dp),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Text(
                stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(Modifier.height(16.dp))
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.MusicNote,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.local_music)) },
            selected = currentDestination == Destination.LocalMusic,
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDestinationSelected(Destination.LocalMusic)
            }
        )
        Spacer(Modifier.height(16.dp))
        NavigationDrawerItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_piped),
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.piped_music)) },
            selected = currentDestination == Destination.PipedMusic,
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDestinationSelected(Destination.PipedMusic)
            }
        )
        Spacer(Modifier.height(16.dp))
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.settings_title)) },
            selected = false,
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDestinationSelected(Destination.Settings)
            }
        )
    }
}
