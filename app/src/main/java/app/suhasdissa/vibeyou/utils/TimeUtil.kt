package app.suhasdissa.vibeyou.utils

import java.time.Instant
import java.time.ZoneId

object TimeUtil {
    fun getYear(timestamp: Long): Int {
        val instant = Instant.ofEpochSecond(timestamp)
        return instant.atZone(ZoneId.systemDefault()).year
    }
}
