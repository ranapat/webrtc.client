package org.ranapat.webrtc.example.ui.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.ranapat.webrtc.example.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible

internal class StickyHeaderItem(private val title: String) : AbstractHeaderItem<StickyHeaderViewHolder>() {

    override fun getLayoutRes(): Int = R.layout.item_stream_sticky_header

    override fun equals(other: Any?): Boolean {
        return (other as? StickyHeaderItem)?.title.equals(title)
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): StickyHeaderViewHolder =
            StickyHeaderViewHolder(view, adapter)

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?, holder: StickyHeaderViewHolder, position: Int, payloads: MutableList<Any>?) {
        holder.bind(title)
    }

}