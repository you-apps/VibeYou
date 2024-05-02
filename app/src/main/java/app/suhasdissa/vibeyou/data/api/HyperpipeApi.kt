package app.suhasdissa.vibeyou.data.api

import app.suhasdissa.vibeyou.backend.models.hyper.NextSongsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface HyperpipeApi {
    @GET("https://{instance}/next/{videoId}")
    suspend fun getNext(
        @Path("instance") instance: String,
        @Path("videoId") videoId: String
    ): NextSongsResponse
}