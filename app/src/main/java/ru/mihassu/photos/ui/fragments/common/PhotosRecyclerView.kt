package ru.mihassu.photos.ui.fragments.common

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import ru.mihassu.photos.App
import ru.mihassu.photos.domain.Photo

class PhotosRecyclerView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr) {

    val rvEventsProcessor: PublishSubject<RecyclerViewEvents> = PublishSubject.create()
    private val disposables = CompositeDisposable()


//    private val scrollEventsObserver : DisposableObserver<Int>
    private val rvAdapter: PhotosRvAdapter

    init {
        setHasFixedSize(true)
        layoutManager = GridLayoutManager(context, 3, VERTICAL, false)
        val picasso = App.appComponent.picasso
        rvAdapter = PhotosRvAdapter(picasso) { rvEventsProcessor.onNext(RecyclerViewEvents.LoadMoreEvent(-1))}
        rvAdapter.setOnPhotoClickListener(object : PhotosRvAdapter.OnPhotoClickListener {
            override fun onPhotoClick(photo: Photo) {
                rvEventsProcessor.onNext(RecyclerViewEvents.PhotoClickEvent(photo))
            }
        })
        adapter = rvAdapter

        val rvListener = RvScrollListener(this) { direction: Int ->
            rvEventsProcessor.onNext(RecyclerViewEvents.ScrollEvent(direction)) }

//        scrollEventsObserver = object : DisposableObserver<Int>() {
//
//                    override fun onNext(lastPosition: Int) {
//                        rvEventsProcessor.onNext(RecyclerViewEvents.LoadMoreEvent(lastPosition))
//                    }
//
//                    override fun onError(t: Throwable) {
//                        Logi.logIt("scrollEventsObserver - onError")
//                    }
//
//                    override fun onComplete() {
//                        Logi.logIt("scrollEventsObserver - onComplete")
//                    }
//                }
//        disposables.add(rvListener.scrollEventsSubject.subscribeWith(scrollEventsObserver))
        addOnScrollListener(rvListener)

    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
//        super.onMeasure(widthSpec, heightSpec)
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        val resolvedWidth = resolveSize(desiredWidth, widthSpec)
        val resolvedHeight = resolveSize(desiredHeight, heightSpec)
        rvAdapter.recyclerWidth = resolvedWidth
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }


    fun setDataList(data: List<Photo>) {
        rvAdapter.setDataList(data)
    }

    fun onPause() {
        disposables.dispose()
    }

}