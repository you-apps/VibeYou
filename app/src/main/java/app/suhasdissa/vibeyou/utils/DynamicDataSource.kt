package app.suhasdissa.vibeyou.utils

import android.net.Uri
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.ResolvingDataSource
import androidx.media3.datasource.TransferListener

/**
 * A dynamic data source, which attempts to play from the cache if the source is online
 * Else, if the source uri is not online, it doesn't cache and plays directly from the device storage
 */
@UnstableApi
class DynamicDataSource(
    private val resolvingDataSource: ResolvingDataSource,
    private val defaultDataSource: DefaultDataSource
) : DataSource {
    private var isOnline = false
    private val dataSource get() = if (isOnline) resolvingDataSource else defaultDataSource

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        return dataSource.read(buffer, offset, length)
    }

    override fun addTransferListener(transferListener: TransferListener) {
        dataSource.addTransferListener(transferListener)
    }

    override fun open(dataSpec: DataSpec): Long {
        isOnline = dataSpec.uri.scheme != "content"
        return dataSource.open(dataSpec)
    }

    override fun getUri(): Uri? = dataSource.uri

    override fun close() {
        dataSource.close()
    }

    companion object {
        class Factory(
            private val resolvingDataSource: ResolvingDataSource.Factory,
            private val defaultDataSource: DefaultDataSource.Factory
        ) : DataSource.Factory {
            override fun createDataSource(): DataSource {
                return DynamicDataSource(
                    resolvingDataSource.createDataSource(),
                    defaultDataSource.createDataSource()
                )
            }
        }
    }
}
