package info.aliavci.aavci.mynotes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.raizlabs.android.dbflow.kotlinextensions.save
import info.aliavci.aavci.mynotes.model.db.LogEntry
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.verticalLayout
import org.joda.time.DateTime
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
                    LogEntry().apply {
                        entryId = UUID.randomUUID().toString()
                        entryTitle = "Sample Title"
                        entryContent = "Sample content"
                        entryDate = DateTime.now().toString()
                        save()
                    }
                }
            }
        }
    }
}