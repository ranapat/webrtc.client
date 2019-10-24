package org.ranapat.webrtc.example.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_empty_stream_list.*
import kotlinx.android.synthetic.main.layout_fast_scroller.*
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.ranapat.webrtc.example.R
import org.ranapat.webrtc.example.Settings
import org.ranapat.webrtc.example.data.entity.Stream
import org.ranapat.webrtc.example.ui.BaseActivity
import org.ranapat.webrtc.example.ui.common.IntentParameters
import org.ranapat.webrtc.example.ui.common.OnItemClickListener
import org.ranapat.webrtc.example.ui.common.States.*
import org.ranapat.webrtc.example.ui.util.*
import org.ranapat.webrtc.example.webrtc.Gateway.CALL_MODE_INITIATE_RING
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
    private lateinit var nextActivity: Class<out AppCompatActivity>
    private lateinit var streamAdapter: FlexibleAdapter<AbstractFlexibleItem<*>>
    private lateinit var streamList: ArrayList<AbstractFlexibleItem<*>>
    private lateinit var streams: List<Stream>

    private var selectedStreamBundle: Bundle? = null
    private var readyOnce: Boolean = false

    private val viewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override val layoutResource: Int = R.layout.activity_main

    override fun baseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initialize()
    }

    override fun onResume() {
        super.onResume()

        searchEditText.post {
            searchEditText?.hideSoftKeyboard()
        }
    }

    override fun onPause() {
        super.onPause()

        searchEditText.post {
            searchEditText?.hideSoftKeyboard()
        }
    }

    override fun initializeUi() {
        //
    }

    override fun initializeListeners() {
        subscription(viewModel.state
                .filter { shallThrottle(it) }
                .throttleFirst(Settings.debounceNavigationInMilliseconds, TimeUnit.MILLISECONDS)
                .subscribeUiThread(this) {
                    when (it) {
                        REDIRECT -> startRedirect(nextActivity, selectedStreamBundle)
                        CLEAN_REDIRECT -> startCleanRedirect(nextActivity, selectedStreamBundle)
                    }
                }
        )
        subscription(viewModel.state
                .filter { !shallThrottle(it) }
                .subscribeUiThread(this) {
                    when (it) {
                        LOADING -> loading()
                        READY -> {
                            readyOnce = true

                            ready()
                        }
                        ERROR -> error()
                    }
                }
        )
        subscription(viewModel.message
                .subscribeUiThread(this) {
                    //
                })
        subscription(viewModel.streams
                .subscribeUiThread(this) {
                    streams = it.sortedBy { stream ->
                        stream.name
                    }

                    if (readyOnce) {
                        ready()
                    }
                })
        subscription(viewModel.selectedStream
                .subscribeUiThread(this) {
                    selectedStreamBundle = Bundle().apply {
                        putSerializable(IntentParameters.STREAM, it)
                        putString(IntentParameters.CALL_MODE, CALL_MODE_INITIATE_RING)
                    }
                })
        subscription(viewModel.next
                .subscribeUiThread(this) {
                    nextActivity = it
                })

        searchOpenButton.setOnClickListener {
            actionBottomBarGroup.isVisible = false
            searchBottomBarGroup.isVisible = true

            searchEditText.showSoftKeyboard()
            searchEditText.post {
                searchEditText?.requestFocus()
            }
        }
        searchCloseButton.setOnClickListener {
            closeSearch()
        }
        searchClearButton.setOnClickListener {
            searchEditText.setText("")
        }
        searchEditText.onEditorActionDone {
            closeSearch()
        }
        searchEditText.textChangedListener {
            this.afterTextChanged {
                streamAdapter.setFilter(searchEditText.text.toString())
                streamAdapter.filterItems()
            }
        }
        recyclerView.onTouch { _, _ ->
            if (searchBottomBarGroup.isVisible) {
                searchEditText?.hideSoftKeyboard()
            }
        }
    }

    private fun closeSearch() {
        actionBottomBarGroup.isVisible = true
        searchBottomBarGroup.isVisible = false
        searchEditText.hideSoftKeyboard()
        searchEditText.setText("")
    }

    private fun loading() {
        //
    }

    private fun ready() {
        createStreamAdapter()

        recyclerView.apply {
            layoutManager = SmoothScrollLinearLayoutManager(this@MainActivity)
            adapter = streamAdapter
            setHasFixedSize(true)
            addItemDecoration(FlexibleItemDecoration(this@MainActivity)
                    .withDivider(R.drawable.layer_shape_recyclerview_divider,
                            R.layout.item_stream)
                    .withDrawDividerOnLastItem(false))
        }

        streamAdapter.fastScroller = fastScrollerView
        streamAdapter.setDisplayHeadersAtStartUp(true)
                .setStickyHeaders(true)

        EmptyViewHelper.create(streamAdapter, null, emptyStreamList)
    }

    private fun createStreamAdapter() {
        val arrayLetterIndex = mutableListOf<Int>()

        streamList = ArrayList()
        var lastLetter: Char = Char.MIN_VALUE
        lateinit var stickyHeader: StickyHeaderItem

        streams.forEachIndexed { index, stream ->
            //set new sticky header for new letter
            val newLetter = stream.name.first()
            if (newLetter != lastLetter) {
                lastLetter = newLetter
                stickyHeader = StickyHeaderItem(lastLetter.toString())
                arrayLetterIndex.add(index)
            }

            streamList.add(StreamViewItem(stream, itemClickListener, stickyHeader))
        }
        streamAdapter = StreamAdapter(streamList, arrayLetterIndex)
    }

    private val itemClickListener = OnItemClickListener<Stream> { stream ->
        viewModel.call(stream)
    }

    private fun error() {
        //
    }
}