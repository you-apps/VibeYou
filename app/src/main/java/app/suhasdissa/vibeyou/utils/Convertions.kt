package app.suhasdissa.vibeyou.utils

import kotlin.math.ln
import kotlin.math.pow

fun formatMB(mb: Int): String {
    val index = (ln(mb.toDouble()) / ln(1024.0)).toInt()
    return "${mb.div(1024f.pow(index)).toInt()} ${arrayOf("M", "G", "T")[index]}B"
}
