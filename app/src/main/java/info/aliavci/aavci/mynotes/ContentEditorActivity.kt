package info.aliavci.aavci.mynotes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import info.aliavci.aavci.mynotes.model.db.LogEntry
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.editText
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

    var logEntry: LogEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = ContentEditorActivityUI()
        content.setContentView(this)
        
        title = intent.getStringExtra("TITLE")

        if (title != null) {
            // Edit Mode since title exists
            logEntry = LogEntry.getLogEntry(title.toString())
            content.editTextTitle.setText(logEntry?.entryTitle)
            content.editTextContent.setText(logEntry?.entryContent)
        } else {
            title = getString(R.string.editing_file, "Sample File")
        }
    }

    inner class ContentEditorActivityUI : AnkoComponent<ContentEditorActivity> {

        lateinit var editTextTitle: EditText
        lateinit var editTextContent: EditText

        override fun createView(ui: AnkoContext<ContentEditorActivity>) = with(ui) {
            verticalLayout {
                padding = dip(25)
                editTextTitle = editText {
                    hint = "Title"
                    textSize = 16f
                    addTextChangedListener(object: TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {}

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            title = p0
                        }

                    })
                }
                editTextContent = editText {
                    hint = "Content"
                    textSize = 12f
                }
                button("Save Text") {
                    onClick {
                        if (logEntry != null) {
                            logEntry?.apply {
                                entryTitle = editTextTitle.text.toString()
                                entryContent = editTextContent.text.toString()
                                save()
                            }
                        } else {
                            LogEntry().apply {
                                entryId = UUID.randomUUID().toString()
                                entryTitle = editTextTitle.text.toString()
                                entryContent = editTextContent.text.toString()
                                entryDate = DateTime.now().toString()
                                save()
                            }
                        }
                        finish()
                    }
                }
            }
        }
    }
}
