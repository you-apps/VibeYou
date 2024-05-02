package app.suhasdissa.vibeyou.backend.models

import androidx.media3.common.Player

/*
DO not change order
   int REPEAT_MODE_OFF = 0;
   int REPEAT_MODE_ONE = 1;
   int REPEAT_MODE_ALL = 2;
 */
enum class PlayerRepeatMode(val mode: Int) {
    OFF(Player.REPEAT_MODE_OFF),
    ONE(Player.REPEAT_MODE_ONE),
    ALL(Player.REPEAT_MODE_ALL)
}
