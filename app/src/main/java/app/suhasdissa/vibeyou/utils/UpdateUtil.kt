package app.suhasdissa.vibeyou.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.regex.Pattern

object UpdateUtil {
    var currentVersion = 0f

    private suspend fun getLatestRelease(): LatestRelease? {
        return try {
            UpdateApi.retrofitService.getLatestRelease()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getLatestVersion(): Float? {
        return getLatestRelease()?.let {
            it.tagName.toVersion()
        }
    }

    fun getCurrentVersion(context: Context) {
        currentVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0)
            ).versionName.toFloat()
        } else {
            context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).versionName.toFloat()
        }
    }

    private val pattern = Pattern.compile("""v(.+)""")

    private fun String?.toVersion(): Float = this?.run {
        val matcher = pattern.matcher(this)
        if (matcher.find()) {
            matcher.group(1)?.toFloat() ?: 0f
        } else {
            0f
        }
    } ?: 0f
}

@Serializable
data class LatestRelease(
    @SerialName("tag_name") val tagName: String? = null
)

private val jsonFormat = Json { ignoreUnknownKeys = true }

private val retrofitVideo = Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
    .build()

interface UpdateApiService {
    @Headers(
        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36"
    )
    @GET("repos/you-apps/VibeYou/releases/latest")
    suspend fun getLatestRelease(): LatestRelease
}

object UpdateApi {
    val retrofitService: UpdateApiService by lazy {
        retrofitVideo.create(UpdateApiService::class.java)
    }
}
