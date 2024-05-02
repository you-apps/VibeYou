package app.suhasdissa.vibeyou.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.suhasdissa.vibeyou.R

@Composable
fun IllustratedMessageScreen(
    @DrawableRes image: Int,
    @StringRes message: Int? = null,
    messageColor: Color = MaterialTheme.colorScheme.error,
    action: @Composable () -> Unit = {}
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        message?.let {
            Text(
                stringResource(message),
                style = MaterialTheme.typography.displaySmall,
                color = messageColor,
                modifier = Modifier.alpha(0.5f)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .alpha(0.3f)
        ) {
            Image(
                modifier = Modifier.size(350.dp),
                painter = painterResource(id = R.drawable.blob),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer)
            )
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(id = image),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
            )
        }
        action()
    }
}

@Composable
@Preview(showBackground = true)
private fun IllustratedMsgScreenPreview() {
    IllustratedMessageScreen(
        image = R.drawable.ic_launcher_monochrome,
        message = R.string.something_went_wrong
    )
}
