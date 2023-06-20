package app.suhasdissa.mellowmusic.backend.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.suhasdissa.mellowmusic.backend.database.dao.SearchDao
import app.suhasdissa.mellowmusic.backend.database.dao.SongsDao
import app.suhasdissa.mellowmusic.backend.database.entities.SearchQuery
import app.suhasdissa.mellowmusic.backend.database.entities.Song

@Database(
    entities = [
        Song::class,
        SearchQuery::class,
    ], version = 1, exportSchema = false
)
abstract class SongDatabase : RoomDatabase() {

    abstract fun songsDao(): SongsDao
    abstract fun searchDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getDatabase(context: Context): SongDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, SongDatabase::class.java, "song_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}