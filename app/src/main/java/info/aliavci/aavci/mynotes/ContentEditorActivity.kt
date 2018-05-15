package info.aliavci.aavci.mynotes

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
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
                    onClick {
                        // Get Current Date
                        val c = Calendar.getInstance()
                        val mYear = c.get(Calendar.YEAR)
                        val mMonth = c.get(Calendar.MONTH)
                        val mDay = c.get(Calendar.DAY_OF_MONTH)

                        val datePickerDialog = DatePickerDialog(getContext(),
                                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                                    val simpleDateFormat = SimpleDateFormat("EEE", Locale.ENGLISH)

                                    val cal = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, year)
                                        set(Calendar.MONTH, monthOfYear)
                                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                    }
                                    val dateRepresentation = cal.time

                                    val selectedMonthString = (monthOfYear + 1).toString().padStart(2, '0')
                                    val selectedDayString = dayOfMonth.toString().padStart(2, '0')

                                    setText("Log-$selectedDayString-$selectedMonthString-" +
                                            "${year.toString().takeLast(2)}-" +
                                            "${simpleDateFormat.format(dateRepresentation)}.txt")

                                }, mYear, mMonth, mDay)
                        datePickerDialog.show()
                    }
                }
                editTextContent = editText {
                    hint = "Content"
                    textSize = 12f
                }
                button("Save Text") {
                    setOnClickListener {
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
