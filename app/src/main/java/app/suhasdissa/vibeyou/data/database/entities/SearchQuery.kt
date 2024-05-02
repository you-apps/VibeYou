package app.suhasdissa.vibeyou.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(
            value = ["query"],
            unique = true
        )
    ]
)
data class SearchQuery(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String
)
