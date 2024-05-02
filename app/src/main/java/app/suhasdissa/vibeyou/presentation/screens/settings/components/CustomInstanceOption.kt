package app.suhasdissa.vibeyou.presentation.screens.settings.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.backend.models.PipedInstance
import app.suhasdissa.vibeyou.utils.Pref
import app.suhasdissa.vibeyou.utils.rememberPreference
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun String.isValidBaseUrl(): Boolean {
    val url = toHttpUrlOrNull() ?: return false
    return url.encodedPath in listOf("", "/")
}

@Composable
fun CustomInstanceOption(
    onInstanceChange: (PipedInstance) -> Unit
) {
    val context = LocalContext.current

    var isUsingValidCustomInstance = remember {
        Pref.sharedPreferences.getBoolean(Pref.customPipedInstanceKey, false)
    }
    var showCustomInstanceDialog by remember {
        mutableStateOf(false)
    }
    var useCustomInstanceChecked by rememberPreference(
        key = Pref.customPipedInstanceKey,
        defaultValue = false
    )

    SwitchPref(
        prefKey = Pref.customPipedInstanceKey,
        title = stringResource(R.string.custom_instance)
    ) {
        showCustomInstanceDialog = it

        // reset the instance to the default one
        if (!it) {
            val instance = Pref.pipedInstances.first()
            Pref.setInstance(instance)
            onInstanceChange(instance)
        }
    }

    if (showCustomInstanceDialog) {
        var instanceApiUrl by remember {
            mutableStateOf(Pref.currentInstance.apiUrl)
        }
        var instanceImageProxyUrl by remember {
            mutableStateOf(Pref.currentInstance.imageProxyUrl)
        }

        AlertDialog(
            onDismissRequest = {
                showCustomInstanceDialog = false
                useCustomInstanceChecked = isUsingValidCustomInstance
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val apiUrl = instanceApiUrl.toHttpUrlOrNull()
                        if (apiUrl == null || !instanceApiUrl.isValidBaseUrl()) {
                            Toast.makeText(context, R.string.invalid_url, Toast.LENGTH_SHORT).show()

                            isUsingValidCustomInstance = false
                            useCustomInstanceChecked = false
                        } else {
                            val instance = PipedInstance(
                                name = apiUrl.host,
                                apiUrl = apiUrl.toString(),
                                imageProxyUrl = instanceImageProxyUrl
                                    .takeIf { it.isValidBaseUrl() }
                                    .orEmpty()
                            )

                            Pref.setInstance(instance)
                            onInstanceChange(instance)

                            isUsingValidCustomInstance = true
                        }

                        showCustomInstanceDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCustomInstanceDialog = false
                        useCustomInstanceChecked = isUsingValidCustomInstance
                    }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            title = {
                Text(text = stringResource(R.string.custom_instance))
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = instanceApiUrl,
                        onValueChange = {
                            instanceApiUrl = it
                        },
                        isError = !instanceApiUrl.isValidBaseUrl(),
                        placeholder = {
                            Text(stringResource(R.string.api_url))
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = instanceImageProxyUrl,
                        onValueChange = {
                            instanceImageProxyUrl = it
                        },
                        isError = !instanceImageProxyUrl.isValidBaseUrl(),
                        placeholder = {
                            Text(stringResource(R.string.image_proxy_url))
                        }
                    )
                }
            }
        )
    }
}
