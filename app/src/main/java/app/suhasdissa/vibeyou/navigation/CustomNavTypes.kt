package app.suhasdissa.vibeyou.navigation

import android.os.Bundle
import androidx.navigation.NavType
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

val AlbumType = object : NavType<Album>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: Album) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): Album? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Album {
        return Json.decodeFromString<Album>(URLDecoder.decode(value, "UTF-8"))
    }

    override fun serializeAsValue(value: Album): String {
        return URLEncoder.encode(Json.encodeToString(Album.serializer(), value), "UTF-8")
    }
}

val ArtistType = object : NavType<Artist>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: Artist) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): Artist? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Artist {
        return Json.decodeFromString<Artist>(URLDecoder.decode(value, "UTF-8"))
    }

    override fun serializeAsValue(value: Artist): String {
        return URLEncoder.encode(Json.encodeToString(Artist.serializer(), value), "UTF-8")
    }

}