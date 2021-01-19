package ru.mihassu.photos.ui.fragments.common

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mihassu.photos.common.Logi

class RvScrollListener(recyclerView: RecyclerView, private val onScroll: (Int) -> Unit) : RecyclerView.OnScrollListener() {

    private var layoutManager: GridLayoutManager
    private var lastVisibleItemPosition: Int = 0
    private var itemCount: Int = 0
//    val scrollEventsSubject : PublishSubject<Int>


    init {
        layoutManager = recyclerView.layoutManager as GridLayoutManager
//        scrollEventsSubject = PublishSubject.create()
    }

    companion object {
        const val SCROLL_UP = 1
        const val SCROLL_DOWN = -1
        const val NO_SCROLL = 0
        const val LOAD_THRESHOLD = 30
    }

    private var scrollDirection: Int = NO_SCROLL

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//        if (layoutManager == null) {layoutManager = recyclerView.layoutManager as GridLayoutManager}
        if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            onScroll.invoke(scrollDirection)
        }
        layoutManager.let {
            lastVisibleItemPosition = it.findLastVisibleItemPosition()
            itemCount = it.itemCount
//            Logi.logIt("\nitemCount: $itemCount\n lastVisibleItemPosition: $lastVisibleItemPosition\n")
            if (lastVisibleItemPosition >= itemCount - LOAD_THRESHOLD) {
//                scrollEventsSubject.onNext(itemCount)
            }
        }

    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        updateDirection(dy)
    }

    private fun updateDirection(dy: Int) {
        scrollDirection = when {
            dy > 0 -> SCROLL_DOWN
            dy < 0 -> SCROLL_UP
            else -> NO_SCROLL
        }
    }
}