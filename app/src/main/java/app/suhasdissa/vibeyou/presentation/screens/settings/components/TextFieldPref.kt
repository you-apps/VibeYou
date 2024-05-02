package app.suhasdissa.vibeyou.presentation.screens.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import app.suhasdissa.vibeyou.utils.Pref

@Composable
fun TextFieldPref(
    key: String,
    defaultValue: String,
    title: String,
    onValueChange: (String) -> Unit = {}
) {
    var value by remember {
        mutableStateOf(Pref.sharedPreferences.getString(key, defaultValue).orEmpty())
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        value = value,
        onValueChange = {
            value = it
            Pref.sharedPreferences.edit(true) { putString(key, it) }
            onValueChange(it)
        },
        label = {
            Text(text = title)
        }
    )
}