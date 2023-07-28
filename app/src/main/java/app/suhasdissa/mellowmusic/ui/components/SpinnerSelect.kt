package app.suhasdissa.mellowmusic.ui.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView

@Composable
inline fun <reified T : Enum<T>> SpinnerSelector(
    crossinline onItemSelected: (T) -> Unit,
    defaultValue: T
) {
    val options = enumValues<T>()
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultValue) }
    val view = LocalView.current
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        TextButton(
            onClick = { expanded = true }
        ) {
            Icon(Icons.Default.ArrowDropDown, null)
            Text(selectedOption.name)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option.name)
                    },
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        selectedOption = option
                        expanded = false
                        onItemSelected(option)
                    }
                )
            }
        }
    }
}
