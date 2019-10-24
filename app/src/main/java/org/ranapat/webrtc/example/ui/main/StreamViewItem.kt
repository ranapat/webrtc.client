package org.ranapat.webrtc.example.ui.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flexibleadapter.items.ISectionable
import eu.davidea.flexibleadapter.utils.FlexibleUtils
import org.ranapat.webrtc.example.R
import org.ranapat.webrtc.example.data.entity.Stream
import org.ranapat.webrtc.example.ui.common.OnItemClickListener

internal class StreamViewItem(
        val stream: Stream,
        private val itemClickListener: OnItemClickListener<Stream>,
        private var headerItem: StickyHeaderItem
) : AbstractFlexibleItem<StreamViewHolder>(),
        ISectionable<StreamViewHolder, StickyHeaderItem>,
        IFilterable<String> {

    override fun getLayoutRes(): Int = R.layout.item_stream

    override fun equals(other: Any?): Boolean {
        return (other as? Stream)?.id == stream.id
    }

    override fun hashCode(): Int {
        return stream.hashCode()
    }

    override fun getHeader(): StickyHeaderItem = headerItem

    override fun setHeader(header: StickyHeaderItem) {
        headerItem = header
    }

    override fun filter(constraint: String): Boolean {
        return stream.name.contains(constraint, true)
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): StreamViewHolder =
            StreamViewHolder(view, adapter)

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: StreamViewHolder, position: Int, payloads: MutableList<Any>) {

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(stream)
        }

        if (adapter.hasFilter()) {
            val filter = adapter.getFilter(String::class.java)
            FlexibleUtils.highlightWords(holder.streamTextView, stream.name, filter)
        } else {
            holder.streamTextView.text = stream.name
        }
    }
}