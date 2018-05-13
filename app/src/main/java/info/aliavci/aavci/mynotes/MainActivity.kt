package info.aliavci.aavci.mynotes

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.FrameLayout
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import info.aliavci.aavci.mynotes.model.db.LogEntry
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.alignParentBottom
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import timber.log.Timber

/**
 * Created by Ali Avci
 * Version
 */
class MainActivity : AppCompatActivity() {

    val notesAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        downloadContent()

        val boringFancyItems = getLocalData()

        ExpandableGroup(ExpandableHeaderItem("List of Notes"), true).apply {
            add(Section(boringFancyItems))
            notesAdapter.add(this)
        }

        MainActivityUI(notesAdapter).setContentView(this)
    }

    override fun onResume() {
        super.onResume()
        getLocalData()
    }

    /**
     * Get notes from DB
     */
    private fun getLocalData(): MutableList<FancyItem>{
        val listOfNotes = LogEntry.getLogEntries()
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
}

class MainActivityUI(val groupAdapter: GroupAdapter<ViewHolder>) : AnkoComponent<MainActivity> {
    @SuppressLint("ResourceType")
    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        return relativeLayout {
            padding = dip(25)

            // BUTTON
            button("New Note") {
                setOnClickListener {
                    startActivity<ContentEditorActivity>()
                }
            }.lparams {
                alignParentBottom()
                width = matchParent
            }

            // LIST
            recyclerView {
                id = R.id.recycler_view
                layoutManager = GridLayoutManager(context, groupAdapter.spanCount).apply {
                    spanSizeLookup = groupAdapter.spanSizeLookup
                }
                adapter = groupAdapter
            }.lparams {
                width = matchParent
                height = matchParent
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