package app.suhasdissa.vibeyou.presentation.screens.settings.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.models.PipedInstance
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsViewModel
import app.suhasdissa.vibeyou.utils.Pref

@Composable
fun InstanceSelectDialog(
    onDismissRequest: () -> Unit,
    onSelectionChange: (instance: PipedInstance) -> Unit
) {
    val viewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)

    var selectedInstance by remember {
        mutableStateOf(Pref.currentInstance)
    }
    val view = LocalView.current

    AlertDialog(
        onDismissRequest = { onDismissRequest.invoke() },
        confirmButton = {
            TextButton(onClick = { onDismissRequest.invoke() }) {
                Text(text = stringResource(android.R.string.cancel))
            }
        },
        title = {
            Text(stringResource(R.string.select_server))
        },
        text = {
            Surface(
                modifier = Modifier.width(300.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                LazyColumn(modifier = Modifier.height(500.dp)) {
                    items(items = viewModel.instances) { item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(onClick = {
                                    view.playSoundEffect(SoundEffectConstants.CLICK)
                                    selectedInstance = item
                                    onSelectionChange(item)
                                    onDismissRequest.invoke()
                                })
                                .padding(horizontal = 6.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedInstance.name == item.name),
                                onClick = null
                            )
                            Text(
                                text = item.name,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}
