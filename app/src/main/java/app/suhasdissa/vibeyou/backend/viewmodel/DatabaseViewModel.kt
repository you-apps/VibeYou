package app.suhasdissa.vibeyou.backend.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.sqlite.db.SimpleSQLiteQuery
import app.suhasdissa.vibeyou.MellowMusicApplication
import app.suhasdissa.vibeyou.backend.database.SongDatabase
import app.suhasdissa.vibeyou.backend.services.PlayerService
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.system.exitProcess
import kotlinx.coroutines.launch

class DatabaseViewModel(private val database: SongDatabase) : ViewModel() {

    init {
    }

    fun backupDatabase(uri: Uri, context: Context) {
        viewModelScope.launch {
            database.rawDao().raw(SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL)"))

            context.applicationContext.contentResolver.openOutputStream(uri)
                ?.use { outputStream ->
                    FileInputStream(database.openHelper.writableDatabase.path).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
        }
    }

    fun restoreDatabase(uri: Uri, context: Context) {
        viewModelScope.launch {
            database.rawDao().raw(SimpleSQLiteQuery("PRAGMA wal_checkpoint(FULL)"))
            database.close()

            context.applicationContext.contentResolver.openInputStream(uri)
                ?.use { inputStream ->
                    FileOutputStream(
                        database.openHelper.writableDatabase.path
                    ).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            context.stopService(Intent(context, PlayerService::class.java))
            exitProcess(0)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                DatabaseViewModel(
                    application.container.database
                )
            }
        }
    }
}
