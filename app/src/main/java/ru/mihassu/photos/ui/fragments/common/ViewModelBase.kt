package ru.mihassu.photos.ui.fragments.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.ui.db.DataBaseInteractor

open class ViewModelBase(dbInteractor: DataBaseInteractor): ViewModel() {

    companion object {
        const val FIRST_PAGE = 1
        const val SECOND_PAGE = 2
    }

    protected val disposables = CompositeDisposable()
    protected val photosLiveData = MutableLiveData<PhotosCallback>()
    protected var pageNumber = 1
    protected var totalPages = 10
    protected var isFirstQuery = true
    protected var dataListState: DataListState = DataListState(dbInteractor)

    init {
        subscribeToDataListUpdate()
    }

    fun getPhotosLiveData(): LiveData<PhotosCallback> = photosLiveData


    private fun subscribeToDataListUpdate() {
        val dataListObserver: DisposableObserver<MutableList<Photo>> = object : DisposableObserver<MutableList<Photo>>() {

            override fun onNext(updatedList: MutableList<Photo>) {
                photosLiveData.value = PhotosCallback.PhotosLoaded(updatedList)
            }

            override fun onError(t: Throwable) {
                photosLiveData.value = PhotosCallback.PhotosError(t)
            }

            override fun onComplete() {}
        }
        disposables.add(dataListState.dataListProcessor.subscribeWith(dataListObserver))
    }

    fun addToCache(photo: Photo): Completable {
        return dataListState.addToCacheBase(photo)
    }


    fun onRefresh() {
        pageNumber = FIRST_PAGE
        dataListState.clear()
        isFirstQuery = true
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        dataListState.clear()
    }
}