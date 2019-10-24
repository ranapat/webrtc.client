package org.ranapat.webrtc.example.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

internal class StreamAdapter(
        private val item: List<AbstractFlexibleItem<*>>,
        private val arrayLetterIndex: MutableList<Int>
) :
        FlexibleAdapter<AbstractFlexibleItem<*>>(
                item,
                null,
                true
        ) {

    private var middlePosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        middlePosition = (item.size + arrayLetterIndex.size) / 2
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onCreateBubbleText(position_: Int): String {

        val firstVisiblePosition = (flexibleLayoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val lastVisiblePosition = (flexibleLayoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

        var optimalItemPosition = if (middlePosition > firstVisiblePosition) firstVisiblePosition else lastVisiblePosition

        var minusPosition = 0
        arrayLetterIndex.forEachIndexed { index, value ->
            if (optimalItemPosition > value) {
                minusPosition = index
            }
        }

        optimalItemPosition -= minusPosition
        optimalItemPosition = if (optimalItemPosition >= item.size) item.size - 1 else optimalItemPosition

        return (item[optimalItemPosition] as? StreamViewItem)?.stream?.name?.first().toString()
    }
}