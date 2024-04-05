package app.suhasdissa.vibeyou.backend.api

import app.suhasdissa.vibeyou.backend.models.hyper.NextSongsResponse
import app.suhasdissa.vibeyou.utils.Pref
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.http.GET
import retrofit2.http.Path

interface HyperpipeApi {
    @GET("https://{instance}/next/{videoId}")
    suspend fun getNext(
        @Path("instance") instance: String = Pref.sharedPreferences
            .getString(Pref.hyperpipeApiUrlKey, "")
            ?.toHttpUrlOrNull()
            ?.host.orEmpty(),
        @Path("videoId") videoId: String
    ): NextSongsResponse
}