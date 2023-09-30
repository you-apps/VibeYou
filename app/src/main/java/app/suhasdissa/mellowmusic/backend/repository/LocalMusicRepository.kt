package app.suhasdissa.mellowmusic.backend.repository

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateUtils
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.SearchFilter
import app.suhasdissa.mellowmusic.backend.models.artists.Artist
import app.suhasdissa.mellowmusic.backend.models.artists.Channel
import app.suhasdissa.mellowmusic.backend.models.artists.ChannelTab
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist
import app.suhasdissa.mellowmusic.backend.models.playlists.PlaylistInfo
import app.suhasdissa.mellowmusic.backend.models.songs.SongItem

class LocalMusicRepository(
    songsDao: SongsDao,
    searchDao: SearchDao,
    private val contentResolver: ContentResolver
) : MusicRepository(searchDao, songsDao) {
    private var songsCache = listOf<Song>()

    private fun getAllSongs(): List<Song> {
        if (songsCache.isNotEmpty()) return songsCache

        val songs = mutableListOf<Song>()

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM
        )

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        val query = contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getLong(durationColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getString(albumColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                songs.add(
                    Song(
                        id = contentUri.toString(),
                        title = name,
                        durationText = DateUtils.formatElapsedTime(duration / 1000),
                        thumbnailUrl = contentUri.toString(),
                        totalPlayTimeMs = duration,
                        artistsText = artist,
                        album = album
                    )
                )
            }
        }

        this.songsCache = songs

        return songs
    }

    override suspend fun getAudioSource(id: String): Uri {
        return id.toUri()
    }

    override suspend fun getRecommendedSongs(id: String): List<MediaItem> = listOf()

    override suspend fun getSuggestions(query: String): List<String> = listOf()

    override suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song> {
        val lowerQuery = query.lowercase()
        return getAllSongs().filter { it.title.lowercase().contains(lowerQuery) }
    }

    override suspend fun getPlaylistResult(query: String, filter: SearchFilter): List<Playlist> {
        val lowerQuery = query.lowercase()
        return getAllSongs()
            .groupBy { it.album }
            .map { Playlist(url = it.key.toString(), name = it.key.toString()) }
            .distinct()
            .filter { it.name.lowercase().contains(lowerQuery) }
    }

    override suspend fun getArtistResult(query: String): List<Artist> {
        val lowerQuery = query.lowercase()
        return getAllSongs()
            .filter { it.artistsText?.lowercase()?.contains(lowerQuery) == true }
            .map { Artist(url = it.artistsText!!, name = it.artistsText) }
    }

    override suspend fun getPlaylistInfo(playlistId: String): PlaylistInfo {
        val songs = getAllSongs()
            .filter { it.album == playlistId }
            .map {
                SongItem(
                    url = it.id,
                    title = it.title,
                    thumbnail = it.thumbnailUrl.orEmpty(),
                    duration = it.totalPlayTimeMs.toInt() / 1000
                )
            }
        return PlaylistInfo(name = playlistId, relatedStreams = ArrayList(songs))
    }

    override suspend fun getChannelInfo(channelId: String): Channel {
        return Channel(id = channelId, name = channelId)
    }

    override suspend fun getChannelPlaylists(channelId: String, tabs: List<ChannelTab>): List<Playlist>? {
        return getAllSongs()
            .filter { it.artistsText == channelId }
            .map { Playlist(url = it.id, uploaderName = it.artistsText.orEmpty(), thumbnail = it.thumbnailUrl.orEmpty(), name = it.title) }
    }

    override suspend fun searchSongId(id: String): Song? {
        return getAllSongs().firstOrNull { it.id == id }?.also { songsDao.addSong(it) }
    }

    companion object {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}