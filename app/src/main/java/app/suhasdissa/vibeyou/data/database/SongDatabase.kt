package app.suhasdissa.vibeyou.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.suhasdissa.vibeyou.data.database.dao.PlaylistDao
import app.suhasdissa.vibeyou.data.database.dao.RawDao
import app.suhasdissa.vibeyou.data.database.dao.SearchDao
import app.suhasdissa.vibeyou.data.database.dao.SongsDao
import app.suhasdissa.vibeyou.data.database.entities.PlaylistEntity
import app.suhasdissa.vibeyou.data.database.entities.SearchQuery
import app.suhasdissa.vibeyou.data.database.entities.SongEntity
import app.suhasdissa.vibeyou.data.database.entities.SongPlaylistMap

@Database(
    entities = [
        SongEntity::class,
        SearchQuery::class,
        PlaylistEntity::class,
        SongPlaylistMap::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songsDao(): SongsDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun searchDao(): SearchDao
    abstract fun rawDao(): RawDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
