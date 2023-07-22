package app.suhasdissa.mellowmusic.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openBrowser(context: Context, url: String) {
    val viewIntent: Intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(url)
    }
    context.startActivity(viewIntent)
}
