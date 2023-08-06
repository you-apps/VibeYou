package app.suhasdissa.mellowmusic.backend.repository

import app.suhasdissa.mellowmusic.backend.api.PipedApi
import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.entities.SearchQuery
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.models.SearchFilter
import app.suhasdissa.mellowmusic.utils.asSong

interface SearchRepository {
    suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song>
    suspend fun getSuggestions(query: String): List<String>
    suspend fun saveSearchQuery(query: String)
    suspend fun getSearchHistory(): List<SearchQuery>
}

class SearchRepositoryImpl(private val searchDao: SearchDao, private val pipedApi: PipedApi) :
    SearchRepository {
    override suspend fun getSearchResult(query: String, filter: SearchFilter): List<Song> {
        return pipedApi.searchPiped(query = query, filter = filter.value).items.map {
            it.asSong
        }
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
