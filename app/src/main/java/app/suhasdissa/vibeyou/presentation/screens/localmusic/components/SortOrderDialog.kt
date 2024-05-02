package app.suhasdissa.vibeyou.presentation.screens.localmusic.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.presentation.components.ChipSelector

enum class SortOrder {
    Alphabetic,
    Creation_Date,
    Date_Added,
    Artist_Name
}

@Composable
fun SortOrderDialog(
    onDismissRequest: () -> Unit,
    onSortOrderChange: (order: SortOrder, reverse: Boolean) -> Unit,
    defaultSortOrder: SortOrder,
    defaultReverse: Boolean
) {
    var sortOrder by remember {
        mutableStateOf(defaultSortOrder)
    }
    var reverse by remember {
        mutableStateOf(defaultReverse)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(R.string.sort_order))
        },
        text = {
            Column {
                ChipSelector(
                    onItemSelected = {
                        sortOrder = it
                    },
                    defaultValue = sortOrder
                )
                val interactionSource = remember { MutableInteractionSource() }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(interactionSource = interactionSource, indication = null) {
                            reverse = !reverse
                        }
                ) {
                    Checkbox(checked = reverse, onCheckedChange = { reverse = it })
                    Text(text = stringResource(R.string.reversed))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSortOrderChange(sortOrder, reverse)
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
