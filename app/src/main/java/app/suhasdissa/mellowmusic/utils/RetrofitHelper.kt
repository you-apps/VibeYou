package app.suhasdissa.mellowmusic.utils

import app.suhasdissa.mellowmusic.backend.api.PipedApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitHelper {
    fun createPipedApi(): PipedApi {
        val json = Json { ignoreUnknownKeys = true }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://pipedapi.kavin.rocks/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create(PipedApi::class.java)
    }
}