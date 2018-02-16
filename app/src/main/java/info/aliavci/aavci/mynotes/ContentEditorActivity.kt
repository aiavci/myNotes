package info.aliavci.aavci.mynotes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.raizlabs.android.dbflow.kotlinextensions.save
import info.aliavci.aavci.mynotes.model.db.DiaryLogEntry
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
                    val diaryLogEntry = DiaryLogEntry()
                    diaryLogEntry.entryId = UUID.randomUUID().toString()
                    diaryLogEntry.entryTitle = "Sample Title"
                    diaryLogEntry.save()
                }
            }
        }
    }
}