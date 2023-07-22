package app.suhasdissa.mellowmusic.backend.repository

import androidx.media3.common.MediaItem
import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.utils.asMediaItem
import app.suhasdissa.mellowmusic.utils.asSong

interface RadioRepository {
    suspend fun getRecommendedSongs(id: String): List<MediaItem>
}

class RadioRepositoryImpl(private val songsDao: SongsDao) : RadioRepository {
    override suspend fun getRecommendedSongs(id: String): List<MediaItem> {
        val relatedSongs =
            PipedApi.retrofitService.getStreams(id).relatedStreams.slice(0..1).map {
                it.asSong
            }
        songsDao.addSongs(relatedSongs)
        return relatedSongs.map { it.asMediaItem }
    }
}
