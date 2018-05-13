package info.aliavci.aavci.mynotes

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textView
import timber.log.Timber
import java.util.*

/**
 * Created by Ali Avci
 * Version
 */
class MainActivity : AppCompatActivity() {

    val data = mutableListOf("test", "Test")

    private val excitingSection = Section()

//    val notesAdapter by lazy { NotesAdapter(data, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        downloadContent()
        getLocalData()

        val groupAdapter = GroupAdapter<ViewHolder>()

        val boringFancyItems = generateFancyItems(24)

        ExpandableGroup(ExpandableHeaderItem("Boring Group"), true).apply {
            add(Section(boringFancyItems))
            groupAdapter.add(this)
        }


        MainActivityUI(groupAdapter).setContentView(this)

//        fab.setOnClickListener {
//            excitingFancyItems.shuffle()
//            excitingSection.update(excitingFancyItems)
//        }
    }

    private fun generateFancyItems(count: Int): MutableList<FancyItem>{
        val rnd = Random()
        return MutableList(count){
            val color = Color.argb(255, rnd.nextInt(256),
                    rnd.nextInt(256), rnd.nextInt(256))
            FancyItem(color, "Sample title")
        }
    }

    override fun onResume() {
        super.onResume()
        getLocalData()
    }

    private fun getLocalData() {
        val listOfNotes = LogEntry.getLogEntries()
//        notesAdapter.update(listOfNotes.map { it.entryTitle }.toMutableList())
    }

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

            val emptyView = textView("Say something outrageous.") {
                textSize = 16f
                typeface = Typeface.MONOSPACE
            }.lparams {
                centerInParent()
            }

            // BUTTON
            button("New Note") {
                onClick {
                    startActivity<ContentEditorActivity>()
                }
            }.lparams {
                alignParentBottom()
                width = matchParent
            }

            fun updateEmptyViewVisibility(recyclerView: RecyclerView) {
                if (doesListHaveItem(recyclerView)) {
                    emptyView.visibility = View.GONE
                } else {
                    emptyView.visibility = View.VISIBLE
                }
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

    private fun doesListHaveItem(list: RecyclerView?) = getListItemCount(list) > 0

    private fun getListItemCount(list: RecyclerView?) = list?.adapter?.itemCount ?: 0
}