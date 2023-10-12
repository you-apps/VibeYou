package app.suhasdissa.vibeyou.backend.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.suhasdissa.vibeyou.backend.database.entities.SearchQuery

@Dao
interface SearchDao {
    @Insert(entity = SearchQuery::class, onConflict = OnConflictStrategy.REPLACE)
    fun addSearchQuery(query: SearchQuery)

    @Query("SELECT * from SearchQuery")
    fun getSearchHistory(): List<SearchQuery>
}
