package app.suhasdissa.vibeyou.utils

import app.suhasdissa.vibeyou.data.api.HyperpipeApi
import app.suhasdissa.vibeyou.data.api.PipedApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

object RetrofitHelper {
    val json by lazy {
        Json { ignoreUnknownKeys = true }
    }

    fun createPipedApi(): PipedApi {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://pipedapi.kavin.rocks/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create()
    }

    fun createHyperpipeApi(): HyperpipeApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hyperpipeapi.onrender.com")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        return retrofit.create()
    }
}
