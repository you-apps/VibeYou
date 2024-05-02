package app.suhasdissa.vibeyou.domain.models.primary

import android.net.Uri
import android.os.Parcelable
import app.suhasdissa.vibeyou.utils.UriSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Artist(
    val id: String,
    val artistsText: String,
    @Serializable(with = UriSerializer::class)
    val thumbnailUri: Uri? = null,
    val description: String? = null,
    val numberOfTracks: Int? = null,
    val numberOfAlbums: Int? = null
) : Parcelable
