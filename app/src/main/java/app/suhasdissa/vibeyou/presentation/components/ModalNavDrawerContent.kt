package app.suhasdissa.vibeyou.presentation.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.navigation.Destination

@Composable
fun ModalNavDrawerContent(
    currentDestination: Destination,
    onDestinationSelected: (Destination) -> Unit,
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
            selected = currentDestination is Destination.LocalMusic,
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
            selected = currentDestination is Destination.OnlineMusic,
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDestinationSelected(Destination.OnlineMusic)
            }
        )
        Spacer(Modifier.height(16.dp))
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.settings_title)) },
            selected = currentDestination is Destination.Settings,
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDestinationSelected(Destination.Settings)
            }
        )
    }
}

@Composable
fun PermanentNavDrawerContent(
    currentDestination: Destination,
    onDestinationSelected: (Destination) -> Unit,
) {
    val view = LocalView.current
    PermanentDrawerSheet(modifier = Modifier.width(250.dp), drawerTonalElevation = 2.dp) {
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
            selected = currentDestination is Destination.LocalMusic,
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
            selected = currentDestination is Destination.OnlineMusic,
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDestinationSelected(Destination.OnlineMusic)
            }
        )
        Spacer(Modifier.height(16.dp))
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null
                )
            },
            label = { Text(text = stringResource(id = R.string.settings_title)) },
            selected = currentDestination is Destination.Settings,
            onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onDestinationSelected(Destination.Settings)
            }
        )
    }
}
