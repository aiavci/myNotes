package info.aliavci.aavci.mynotes

import android.support.annotation.ColorInt
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_fancy.view.*

/**
 * Created by Ali Avci
 * Version
 */

class FancyItem(@ColorInt private val color: Int, private val title: String): Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            item_fancy_cardView.setCardBackgroundColor(color)
            item_fancy_number.text = title
        }
    }

    override fun getLayout() = R.layout.item_fancy

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount
}