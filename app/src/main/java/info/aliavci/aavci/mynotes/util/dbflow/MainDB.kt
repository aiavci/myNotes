package info.aliavci.aavci.mynotes.util.dbflow

import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.Migration
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration
import info.aliavci.aavci.mynotes.model.db.DiaryLogEntry
import timber.log.Timber

/**
 * Created by Ali Avci
 * Version
 */
@Database(name = MainDB.NAME, version = MainDB.VERSION)
object MainDB {
    const val NAME = "MainDB"
    const val VERSION = 2
}

/**
 * Updates
 */
@Migration(version = MainDB.VERSION, database = MainDB::class)
internal class MainDBMigration1 : AlterTableMigration<DiaryLogEntry>((DiaryLogEntry::class.java)) {

    init {
        renameFrom("LogEntry")
        Timber.d("New name given to LogEntry. It is now DiaryLogEntry.")
    }
}