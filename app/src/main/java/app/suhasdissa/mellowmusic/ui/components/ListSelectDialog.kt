package app.suhasdissa.mellowmusic.ui.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.utils.Pref
import app.suhasdissa.mellowmusic.utils.rememberPreference

@Composable
fun InstanceSelectDialog(
    onDismissRequest: () -> Unit,
    onSelectionChange: (name: String) -> Unit
) {
    var selectedOption by rememberPreference(key = Pref.pipedInstanceKey, defaultValue = 0)
    val optionsList = Pref.pipedInstances
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
                    itemsIndexed(items = optionsList) { index, item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    view.playSoundEffect(SoundEffectConstants.CLICK)
                                    selectedOption = index
                                    onSelectionChange(item.name)
                                })
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    selectedOption = index
                                    onSelectionChange(item.name)
                                    onDismissRequest.invoke()
                                }
                                .padding(horizontal = 6.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (index == selectedOption),
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
