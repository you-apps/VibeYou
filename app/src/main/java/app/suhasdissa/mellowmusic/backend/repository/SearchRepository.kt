package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.entities.SearchQuery
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.SearchFilter
import app.suhasdissa.mellowmusic.backend.models.artists.Artist
import app.suhasdissa.mellowmusic.backend.models.playlists.Playlist
import app.suhasdissa.mellowmusic.utils.asSong

interface SearchRepository {
    suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song>
    suspend fun getPlaylistResult(query: String, filter: SearchFilter): List<Playlist>
    suspend fun getArtistResult(query: String): List<Artist>
    suspend fun getSuggestions(query: String): List<String>
    suspend fun saveSearchQuery(query: String)
    suspend fun getSearchHistory(): List<SearchQuery>
}

class SearchRepositoryImpl(private val searchDao: SearchDao, private val pipedApi: PipedApi) :
    SearchRepository {
    override suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song> {
        if (filter != SearchFilter.Songs && filter != SearchFilter.Videos) {
            throw Exception(
                "Invalid filter for search"
            )
        }
        return pipedApi.searchPiped(query = query, filter = filter.value).items.map {
            it.asSong
        }
    }

    override suspend fun getPlaylistResult(query: String, filter: SearchFilter): List<Playlist> {
        if (filter != SearchFilter.Playlists && filter != SearchFilter.Albums) {
            throw Exception(
                "Invalid filter for search"
            )
        }
        return pipedApi.searchPipedPlaylists(query = query, filter = filter.value).items
    }

    override suspend fun getArtistResult(query: String): List<Artist> {
        return pipedApi.searchPipedArtists(query = query).items
    }

    override suspend fun getSuggestions(query: String): List<String> {
        return pipedApi.getSuggestions(query = query)
    }

    override suspend fun saveSearchQuery(query: String) {
        searchDao.addSearchQuery(SearchQuery(id = 0, query))
    }

    override suspend fun getSearchHistory(): List<SearchQuery> {
        return searchDao.getSearchHistory()
    }
}
