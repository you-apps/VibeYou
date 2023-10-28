package app.suhasdissa.vibeyou.backend.repository

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateUtils
import androidx.core.net.toUri
import app.suhasdissa.vibeyou.backend.data.Album
import app.suhasdissa.vibeyou.backend.data.Artist
import app.suhasdissa.vibeyou.backend.data.Song
import app.suhasdissa.vibeyou.backend.database.dao.SearchDao
import app.suhasdissa.vibeyou.backend.database.entities.SearchQuery
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
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.DATE_ADDED
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
            val creationDateColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.DATE_MODIFIED
            )
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

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
                val albumId = cursor.getLong(albumColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val duration = cursor.getLong(durationColumn) / 1000

                // some non-audio files are returned by the media store and hence have a duration of 0ms
                // these entries are hence skipped to remove bloat from the results
                if (duration == 0L) continue

                songs.add(
                    Song(
                        id = contentUri.toString(),
                        title = name,
                        durationText = DateUtils.formatElapsedTime(duration),
                        thumbnailUri = getAlbumArt(albumId),
                        artistsText = cursor.getString(artistColumn),
                        albumId = albumId,
                        artistId = cursor.getLong(artistIdColumn),
                        isLocal = true,
                        creationDate = cursor.getLong(creationDateColumn),
                        dateAdded = cursor.getLong(dateAddedColumn)
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
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
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
            val songsColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                albums.add(
                    Album(
                        id = id.toString(),
                        title = cursor.getString(
                            nameColumn
                        ),
                        artistsText = cursor.getString(artistColumn),
                        thumbnailUri = getAlbumArt(id),
                        numberOfSongs = cursor.getInt(songsColumn),
                        isLocal = true
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
            MediaStore.Audio.Artists.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )

        val sortOrder = "${MediaStore.Audio.Artists.ARTIST} ASC"

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
            val noOfAlbums =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS)
            val noOfTracks =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                artists.add(
                    Artist(
                        id = id.toString(),
                        artistsText = cursor.getString(artistColumn),
                        thumbnailUri = null,
                        numberOfAlbums = cursor.getInt(noOfAlbums),
                        numberOfTracks = cursor.getInt(noOfTracks)
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

    suspend fun getAlbumInfo(albumId: Long): List<Song> {
        return getAllSongs()
            .filter { it.albumId == albumId }
    }

    suspend fun getArtistInfo(artistText: String): List<Album> {
        return getAllAlbums()
            .filter { it.artistsText == artistText }
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
