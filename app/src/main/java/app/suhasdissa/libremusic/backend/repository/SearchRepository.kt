package app.suhasdissa.libremusic.backend.repository

import app.suhasdissa.libremusic.backend.api.PipedApi
import app.suhasdissa.libremusic.backend.database.dao.SearchDao
import app.suhasdissa.libremusic.backend.database.entities.SearchQuery
import app.suhasdissa.libremusic.backend.database.entities.Song
import app.suhasdissa.libremusic.utils.asSong

interface SearchRepository {
    suspend fun getSearchResult(query: String): List<Song>
    suspend fun getSuggestions(query: String): List<String>
    suspend fun saveSearchQuery(query: String)
    suspend fun getSearchHistory(): List<SearchQuery>
}

class SearchRepositoryImpl(private val searchDao: SearchDao) : SearchRepository {
    override suspend fun getSearchResult(query: String): List<Song> {
        return PipedApi.retrofitService.searchPiped(query).items.map {
            it.asSong
        }
    }

    override suspend fun getSuggestions(query: String): List<String> {
        return PipedApi.retrofitService.getSuggestions(query)
    }

    override suspend fun saveSearchQuery(query: String) {
        searchDao.addSearchQuery(SearchQuery(id = 0, query))
    }

    override suspend fun getSearchHistory(): List<SearchQuery> {
        return searchDao.getSearchHistory()
    }
}