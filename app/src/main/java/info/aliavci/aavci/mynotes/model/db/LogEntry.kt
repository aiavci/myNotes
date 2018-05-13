package info.aliavci.aavci.mynotes.model.db

import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.raizlabs.android.dbflow.structure.BaseModel
import info.aliavci.aavci.mynotes.util.dbflow.MainDB

/**
 * Created by Ali Avci
 * Version
 */

@Table(database = MainDB::class, allFields = true)
class LogEntry: BaseModel() {

    @PrimaryKey
    lateinit var entryId: String

    lateinit var entryDate: String

    lateinit var entryTitle: String

    lateinit var entryContent: String

    companion object {
        fun getLogEntries() : MutableList<LogEntry>? {
            return SQLite.select()
                    .from<LogEntry>(LogEntry::class.java)
                    .orderBy(LogEntry_Table.entryTitle.asc())
                    .queryList()
        }

        fun getLogEntries(month: String): MutableList<LogEntry>? {
            return SQLite.select()
                    .from<LogEntry>(LogEntry::class.java)
                    .where(LogEntry_Table.entryTitle.like("Log-__-%$month%"))
                    .queryList()
        }

        fun getLogEntry(title: String): LogEntry? {
            return SQLite.select()
                    .from<LogEntry>(LogEntry::class.java)
                    .where(LogEntry_Table.entryTitle.eq(title))
                    .querySingle()
        }
    }
}