package app.suhasdissa.mellowmusic.backend.repository

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateUtils
import androidx.core.net.toUri
import app.suhasdissa.mellowmusic.backend.data.Album
import app.suhasdissa.mellowmusic.backend.data.AlbumInfo
import app.suhasdissa.mellowmusic.backend.data.Artist
import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.entities.SearchQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalMusicRepository(
    private val contentResolver: ContentResolver,
    private val searchDao: SearchDao
) {
    private var songsCache = listOf<Song>()
    private var albumCache = listOf<Album>()
    private var artistCache = listOf<Artist>()

    suspend fun getAllSongs(): List<Song> = withContext(Dispatchers.IO) {
        if (songsCache.isNotEmpty()) return@withContext songsCache

        val songs = mutableListOf<Song>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
        )

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        val query = contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn =
                cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val artistIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name =
                    if (cursor.isNull(titleColumn)) {
                        cursor.getString(nameColumn)
                    } else {
                        cursor.getString(
                            titleColumn
                        )
                    }
                val duration = cursor.getLong(durationColumn)
                val artist = cursor.getString(artistColumn)
                val album = cursor.getLong(albumColumn)
                val artistId = cursor.getLong(artistIdColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                songs.add(
                    Song(
                        id = contentUri.toString(),
                        title = name,
                        durationText = DateUtils.formatElapsedTime(duration / 1000),
                        thumbnailUri = getAlbumArt(album),
                        artistsText = artist,
                        albumId = album,
                        artistId = artistId
                    )
                )
            }
        }

        this@LocalMusicRepository.songsCache = songs

        songs
    }

    suspend fun getAllAlbums(): List<Album> = withContext(Dispatchers.IO) {
        if (albumCache.isNotEmpty()) return@withContext albumCache

        val albums = mutableListOf<Album>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Albums.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST
        )

        val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"

        val query = contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name =
                    cursor.getString(
                        nameColumn
                    )
                val artist = cursor.getString(artistColumn)

                albums.add(
                    Album(
                        id = id.toString(),
                        title = name,
                        artistsText = artist,
                        thumbnailUri = getAlbumArt(id)
                    )
                )
            }
        }

        this@LocalMusicRepository.albumCache = albums

        albums
    }

    suspend fun getAllArtists(): List<Artist> = withContext(Dispatchers.IO) {
        if (artistCache.isNotEmpty()) return@withContext artistCache

        val artists = mutableListOf<Artist>()

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Albums.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST
        )

        val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"

        val query = contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val artist = cursor.getString(artistColumn)

                artists.add(
                    Artist(
                        id = id.toString(),
                        artistsText = artist
                    )
                )
            }
        }

        this@LocalMusicRepository.artistCache = artists

        artists
    }

    suspend fun getSearchResult(query: String): List<Song> {
        val lowerQuery = query.lowercase()
        return getAllSongs().filter {
            it.title.lowercase().contains(lowerQuery)
        }
    }

    suspend fun getAlbumsResult(query: String): List<Album> {
        val lowerQuery = query.lowercase()
        return getAllAlbums().filter {
            it.title.lowercase().contains(lowerQuery)
        }
    }

    suspend fun getArtistResult(query: String): List<Artist> {
        val lowerQuery = query.lowercase()
        return getAllArtists().filter {
            it.artistsText.lowercase().contains(lowerQuery)
        }
    }

    suspend fun getAlbumInfo(albumId: Long): AlbumInfo {
        val songs = getAllSongs()
            .filter { it.albumId == albumId }
        return AlbumInfo(name = "", songs = songs, thumbnailUri = getAlbumArt(albumId))
    }

    private fun getAlbumArt(albumId: Long): Uri {
        val sArtworkUri = "content://media/external/audio/albumart".toUri()
        return ContentUris.withAppendedId(sArtworkUri, albumId)
    }

    fun saveSearchQuery(query: String) = searchDao.addSearchQuery(SearchQuery(id = 0, query))
    fun getSearchHistory() = searchDao.getSearchHistory()

    companion object {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}
