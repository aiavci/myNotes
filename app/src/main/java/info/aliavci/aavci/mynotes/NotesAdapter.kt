package info.aliavci.aavci.mynotes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.verticalPadding
import org.jetbrains.anko.wrapContent

/**
 * Created by Ali Avci
 * Version
 */
class NotesAdapter(var listOfNotes: MutableList<String>, val context: Context) : RecyclerView.Adapter<NoteHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(TextView(parent.context).apply {
            textSize = 20f
            background = context.obtainStyledAttributes(arrayOf(R.attr.selectableItemBackground).toIntArray()).getDrawable(0)
            verticalPadding = context.dip(8)
            isClickable = true
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        })
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val title = listOfNotes[position]
        holder.textView.apply {
            text = title
            onClick { navigateToLogEntry(title) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 1
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

class NoteHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)