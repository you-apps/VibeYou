package app.suhasdissa.vibeyou.backend.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import app.suhasdissa.vibeyou.backend.database.dao.RawDao
import app.suhasdissa.vibeyou.backend.database.dao.SearchDao
import app.suhasdissa.vibeyou.backend.database.dao.SongsDao
import app.suhasdissa.vibeyou.backend.database.entities.SearchQuery
import app.suhasdissa.vibeyou.backend.database.entities.SongEntity

@Database(
    entities = [
        SongEntity::class,
        SearchQuery::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songsDao(): SongsDao
    abstract fun searchDao(): SearchDao
    abstract fun rawDao(): RawDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        private val MIGRATION_1_2 = Migration(1, 2) { database ->
            database.execSQL("ALTER TABLE song ADD COLUMN creationDate INTEGER")
            database.execSQL("ALTER TABLE song ADD COLUMN dateAdded INTEGER")
        }

        fun getDatabase(context: Context): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongDatabase::class.java,
                    "song_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
