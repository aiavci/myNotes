package info.aliavci.aavci.mynotes

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintSet.HORIZONTAL
import android.support.constraint.ConstraintSet.PARENT_ID
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import info.aliavci.aavci.mynotes.model.db.LogEntry
import info.aliavci.aavci.mynotes.model.db.LogEntry.Companion.getLogEntries
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.spinner
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textView
import org.jetbrains.anko.wrapContent
import timber.log.Timber

/**
 * Created by Ali Avci
 * Version
 */
class MainActivity : AppCompatActivity() {

    val notesAdapter = GroupAdapter<ViewHolder>()
    var selectedYear = "18"
    var yearsList = mutableListOf(selectedYear)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        downloadContent()

        updateExpandableGroup()
        MainActivityUI().setContentView(this)
    }

    override fun onResume() {
        super.onResume()

        updateExpandableGroup()
    }

    private fun updateExpandableGroup() {
        notesAdapter.clear()

        val allEntries = getLogEntries() ?: return

        if (allEntries.isEmpty()) {
            return
        }

        val firstMonth = getMonthValue(allEntries.first().entryTitle)
        val lastMonth = getMonthValue(allEntries.last().entryTitle)

        val firstYear = getYearValue(allEntries.first().entryTitle)
        val lastYear = getYearValue(allEntries.last().entryTitle)


        if (firstMonth == null || lastMonth == null ||
                firstYear == null || lastYear == null) {
            return
        }

        yearsList = (firstYear..lastYear).map {
            it.toString()
        }.toMutableList()
        yearsList.reverse()

        for (selectedMonth in firstMonth..lastMonth) {
            val selectedMonthString = selectedMonth.toString().padStart(2, '0')
            ExpandableGroup(ExpandableHeaderItem("Month: $selectedMonthString"), true).apply {
                add(Section(getLocalData(selectedMonthString, selectedYear)))
                notesAdapter.add(this)
            }
        }
    }

    private fun getMonthValue(title: String): Int? {
        val regexCode = "Log-[0-9][0-9]-([^#&]+)-[0-9][0-9].*.txt".toRegex()
        return regexCode.matchEntire(title)?.groups?.get(1)?.value?.toInt()
    }

    private fun getYearValue(title: String): Int? {
        val regexCode = "Log-[0-9][0-9]-[0-9][0-9]-([^#&]+)-.*.txt".toRegex()
        return regexCode.matchEntire(title)?.groups?.get(1)?.value?.toInt()
    }

    /**
     * Get notes from DB
     */
    private fun getLocalData(month: String, year: String): MutableList<FancyItem> {
        val listOfNotes = LogEntry.getLogEntries(month, year) ?: mutableListOf()
        return listOfNotes.map {
            FancyItem(it.entryTitle)
        }.toMutableList()
    }
    /**
     * Downloads data with content and updates DB
     */
    private fun downloadContent() {
        //an extension over string (support GET, PUT, POST, DELETE with httpGet(), httpPut(), httpPost(), httpDelete())
        "http://httpbin.org/get".httpGet().responseString { request, response, result ->
            //do something with response
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    Timber.d("get error $ex")
                }
                is Result.Success -> {
                    val data = result.get()

                    Timber.d("get success $data")

//                    notesAdapter.update(mutableListOf("Log 1", "Log 2"))
                }
            }
        }
    }

    inner class MainActivityUI : AnkoComponent<MainActivity> {
        @SuppressLint("ResourceType")
        override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
            return constraintLayout {
                padding = dip(25)


                linearLayout {
                    id = R.id.spinner_view
                    textView {
                        setText(R.string.select_year)
                    }
                    spinner {
                        adapter = ArrayAdapter(this@MainActivity, R.layout.year_item, yearsList)
                        onItemSelectedListener {
                            onItemSelected { adapterView, _, i, _ ->
                                selectedYear = adapterView?.getItemAtPosition(i).toString()
                                updateExpandableGroup()
                            }
                        }
                    }
                }.lparams {
                    orientation = HORIZONTAL
                    margin = dip(10)
                }

                textView {
                    id = R.id.list_label
                    text = "All Notes For Selected Year"
                    gravity = Gravity.CENTER
                }.lparams(width = matchParent, height = wrapContent) {
                    topToBottom = R.id.spinner_view
                    margin = dip(10)
                }

                // LIST
                recyclerView {
                    id = R.id.recycler_view
                    layoutManager = GridLayoutManager(context, notesAdapter.spanCount).apply {
                        spanSizeLookup = notesAdapter.spanSizeLookup
                    }
                    adapter = notesAdapter
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(10)
                    topToBottom = R.id.list_label
                }

                // BUTTON
                button("New Note") {
                    id = R.id.new_note_button
                    setOnClickListener {
                        startActivity<ContentEditorActivity>()
                    }
                }.lparams(width = matchParent, height = wrapContent) {
                    margin = dip(10)
                    bottomToBottom = PARENT_ID
                }

            }.apply {
                layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
                        .apply {
                            leftMargin = dip(16)
                            rightMargin = dip(16)
                            bottomMargin = dip(16)
                        }

            }
        }
    }
}