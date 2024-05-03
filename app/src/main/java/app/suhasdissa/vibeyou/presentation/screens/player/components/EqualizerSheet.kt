package app.suhasdissa.vibeyou.presentation.screens.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.EqualizerData
import app.suhasdissa.vibeyou.presentation.components.VerticalSlider
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel
import app.suhasdissa.vibeyou.utils.Pref
import app.suhasdissa.vibeyou.utils.rememberPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerSheet(
    equalizerData: EqualizerData,
    playerViewModel: PlayerViewModel,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        CenterAlignedTopAppBar(title = {
            Text(text = stringResource(id = R.string.equalizer), fontSize = 20.sp)
        })
        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        var equalizerEnabled by rememberPreference(key = Pref.equalizerKey, defaultValue = false)

        Row(modifier = Modifier.clickable(interactionSource = remember {
            MutableInteractionSource()
        }, indication = null) {
            equalizerEnabled = equalizerEnabled.not()
            playerViewModel.updateEqualizerSettings()
        }, verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = equalizerEnabled,
                onCheckedChange = {
                    equalizerEnabled = it
                    playerViewModel.updateEqualizerSettings()
                }
            )
            Text(text = stringResource(id = R.string.enabled))
        }

        AnimatedVisibility(
            visible = equalizerEnabled, modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            var selectedPresetIndex by remember {
                mutableIntStateOf(
                    Pref.sharedPreferences.getInt(Pref.equalizerPresetKey, -1) + 1
                )
            }

            Column {
                if (equalizerData.presets.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    val options = listOf(stringResource(R.string.none)) + equalizerData.presets

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                    ) {
                        TextField(
                            modifier = Modifier.menuAnchor(),
                            value = options[selectedPresetIndex].capitalize(Locale.current),
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            label = { Text(stringResource(id = R.string.equalizer_preset)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            options.forEachIndexed { index, option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option.capitalize(Locale.current),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        selectedPresetIndex = index

                                        Pref.sharedPreferences.edit {
                                            putInt(Pref.equalizerPresetKey, index - 1)
                                        }
                                        playerViewModel.updateEqualizerSettings()

                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = selectedPresetIndex == 0) {
                    Column {
                        val bandLevels = remember {
                            val list = Pref.sharedPreferences.getString(Pref.equalizerBandsKey, "")
                                .takeIf { !it.isNullOrEmpty() }?.split(",")?.map(String::toShort)
                            // init at 0 - default/normal band volume
                                ?: List(equalizerData.bands.size) { (0).toShort() }

                            list.toMutableStateList()
                        }

                        fun onUpdateBands() {
                            Pref.sharedPreferences.edit {
                                putString(
                                    Pref.equalizerBandsKey, bandLevels.joinToString(",")
                                )
                            }
                            playerViewModel.updateEqualizerSettings()
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            equalizerData.bands.forEachIndexed { index, band ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    VerticalSlider(
                                        modifier = Modifier.width(300.dp),
                                        value = bandLevels[index].toFloat(),
                                        onValueChange = {
                                            bandLevels[index] = it.toInt().toShort()
                                        },
                                        onValueChangeFinished = {
                                            onUpdateBands()
                                        },
                                        valueRange = band.minLevel.toFloat()..band.maxLevel.toFloat()
                                    )
                                    Text(text = "${band.frequency / 1000}kHz")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(onClick = {
                            for (i in bandLevels.indices) {
                                bandLevels[i] = 0
                            }
                            onUpdateBands()
                        }) {
                            Text(text = stringResource(id = R.string.reset_equalizer))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}