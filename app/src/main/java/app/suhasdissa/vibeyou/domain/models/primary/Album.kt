package app.suhasdissa.vibeyou.domain.models.primary

import android.net.Uri
import android.os.Parcelable
import app.suhasdissa.vibeyou.utils.UriSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Album(
    val id: String,
    val title: String,
    @Serializable(with = UriSerializer::class)
    val thumbnailUri: Uri? = null,
    val artistsText: String,
    val numberOfSongs: Int? = null,
    val isLocal: Boolean = false,
    val type: Type = Type.ALBUM
) : Parcelable {
    enum class Type {
        PLAYLIST, ALBUM
    }
}