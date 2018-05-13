package info.aliavci.aavci.mynotes

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_fancy.view.*
import org.jetbrains.anko.startActivity

/**
 * Created by Ali Avci
 * Version
 */
class FancyItem(private val title: String): Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            item_fancy_number.text = title
            setOnClickListener {
                context.startActivity<ContentEditorActivity>("TITLE" to title)
            }
        }
    }

    override fun getLayout() = R.layout.item_fancy

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount
}