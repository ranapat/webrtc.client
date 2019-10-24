package org.ranapat.webrtc.example.ui.main

import android.view.View
import android.widget.TextView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_stream_sticky_header.view.*

internal class StickyHeaderViewHolder(view: View, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(view, adapter, true) {

    private val titleTextView: TextView = view.titleTextView

    fun bind(title: String) {
        titleTextView.text = title
    }

}