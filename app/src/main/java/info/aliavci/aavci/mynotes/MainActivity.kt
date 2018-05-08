package info.aliavci.aavci.mynotes

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteQuery
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.raizlabs.android.dbflow.sql.language.SQLite
import info.aliavci.aavci.mynotes.model.db.LogEntry
import info.aliavci.aavci.mynotes.model.db.LogEntry_Table
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
import org.jetbrains.anko.verticalPadding
import org.jetbrains.anko.wrapContent
import timber.log.Timber

/**
 * Created by Ali Avci
 * Version
 */
class MainActivity : AppCompatActivity() {

    val data = mutableListOf("test", "Test")

    val notesAdapter by lazy { NotesAdapter(data, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI(notesAdapter).setContentView(this)
//        downloadContent()
        getLocalData()
    }

    private fun getLocalData() {
        val listOfNotes = LogEntry.getLogEntries()
        notesAdapter.update(listOfNotes.map { it.entryTitle }.toMutableList())
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

                    notesAdapter.update(mutableListOf("Log 1", "Log 2"))
                }
            }
        }
    }
}

class MainActivityUI(val listAdapter: NotesAdapter) : AnkoComponent<MainActivity> {
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
                val orientation = LinearLayoutManager.VERTICAL
                layoutManager = LinearLayoutManager(context, orientation, true)
                overScrollMode = View.OVER_SCROLL_NEVER
                adapter = listAdapter
                adapter.registerAdapterDataObserver(
                        object : RecyclerView.AdapterDataObserver() {
                            override fun onItemRangeInserted(start: Int, count: Int) {
                                updateEmptyViewVisibility(this@recyclerView)
                            }

                            override fun onItemRangeRemoved(start: Int, count: Int) {
                                updateEmptyViewVisibility(this@recyclerView)
                            }
                        })

                updateEmptyViewVisibility(this)
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

class Holder(val textView: TextView) : RecyclerView.ViewHolder(textView)

class NotesAdapter(var listOfNotes: MutableList<String>, val context: Context) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(TextView(parent.context).apply {
            textSize = 20f
            background = context.obtainStyledAttributes(arrayOf(R.attr.selectableItemBackground).toIntArray()).getDrawable(0)
            verticalPadding = context.dip(8)
            isClickable = true
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        })
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val title = listOfNotes[position]
        holder.textView.apply {
            text = title
            onClick { navigateToLogEntry(title) }
        }
    }

    private fun navigateToLogEntry(title: String) {
        context.startActivity<ContentEditorActivity>("TITLE" to title)
    }

    override fun getItemCount(): Int = listOfNotes.size

    fun update (newListOfNotes: MutableList<String>) {
        listOfNotes = newListOfNotes
        notifyDataSetChanged()
    }

    fun push(text: String) {
        listOfNotes.add(0, text)
        notifyItemInserted(0)
    }

    fun pop() {
        listOfNotes.remove(listOfNotes.last())
        notifyItemRemoved(listOfNotes.size)
    }
}