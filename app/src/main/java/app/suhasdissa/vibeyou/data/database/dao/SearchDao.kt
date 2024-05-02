package app.suhasdissa.vibeyou.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.suhasdissa.vibeyou.data.database.entities.SearchQuery

@Dao
interface SearchDao {
    @Insert(entity = SearchQuery::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSearchQuery(query: SearchQuery)

    @Query("SELECT * from SearchQuery")
    fun getSearchHistory(): List<SearchQuery>

    @Query("DELETE from SearchQuery")
    fun deleteAll()

    @Query("DELETE from SearchQuery WHERE `query` = :query")
    fun deleteQuery(query: String)
}
