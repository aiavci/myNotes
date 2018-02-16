package info.aliavci.aavci.mynotes.model.db

import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import info.aliavci.aavci.mynotes.util.dbflow.MainDB

/**
 * Created by Ali Avci
 * Version
 */

@Table(database = MainDB::class, allFields = true)
class LogEntry {

    @PrimaryKey
    lateinit var entryId: String

    lateinit var entryTitle: String
}