package info.aliavci.aavci.mynotes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.raizlabs.android.dbflow.sql.language.SQLite
import info.aliavci.aavci.mynotes.model.db.LogEntry
import timber.log.Timber
import android.provider.ContactsContract.CommonDataKinds.Organization
import com.raizlabs.android.dbflow.kotlinextensions.save
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


/**
 * Allows for the modification of the content
 * Created by Ali Avci
 * Version 0.0.1
 */
class ContentEditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContentEditorActivityUI().setContentView(this)

        title = getString(R.string.editing_file, "Sample File")
    }
}
class ContentEditorActivityUI : AnkoComponent<ContentEditorActivity> {
    override fun createView(ui: AnkoContext<ContentEditorActivity>) = with(ui) {
        verticalLayout {
            padding = dip(25)
            button("Save Text") {
                onClick {
                    var logEntry: LogEntry = LogEntry()
                    logEntry.entryId = UUID.randomUUID().toString()
                    logEntry.entryTitle = "Sample Title"
                    logEntry.save()
                }
            }
        }
    }
}