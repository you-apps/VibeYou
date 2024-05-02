package app.suhasdissa.vibeyou.presentation.components

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> ChipSelector(
    crossinline onItemSelected: (T) -> Unit,
    defaultValue: T
) {
    val options = enumValues<T>()
    var selectedOption by remember { mutableStateOf(defaultValue) }
    val view = LocalView.current
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(items = options) {
            FilterChip(selected = (selectedOption == it), onClick = {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                selectedOption = it
                onItemSelected(it)
            }, label = { Text(it.name.replace("_", " ")) })
        }
    }
}
