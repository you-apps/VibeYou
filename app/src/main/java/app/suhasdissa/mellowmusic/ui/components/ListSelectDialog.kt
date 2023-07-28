package app.suhasdissa.mellowmusic.ui.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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

    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Surface(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("Select Server")
                Spacer(modifier = Modifier.height(10.dp))
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
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (index == selectedOption),
                                onClick = {
                                    selectedOption = index
                                    onSelectionChange(item.name)
                                }
                            )
                            Text(
                                text = item.name,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Reopen app to apply changes", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
