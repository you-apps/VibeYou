package app.suhasdissa.mellowmusic.utils

import app.suhasdissa.mellowmusic.backend.api.NewPipeApi
import app.suhasdissa.mellowmusic.backend.api.PipedApi

object ApiHelper {
    fun createPipedApi(): PipedApi {
//        val json = Json { ignoreUnknownKeys = true }
//
//        val retrofit: Retrofit = Retrofit.Builder()
//            .baseUrl("https://pipedapi.kavin.rocks/")
//            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
//            .build()
//
//        return retrofit.create(PipedApi::class.java)
        return NewPipeApi()
    }
}
