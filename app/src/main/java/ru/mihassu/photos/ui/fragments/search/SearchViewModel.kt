package ru.mihassu.photos.ui.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.domain.PhotoPage
import ru.mihassu.photos.interactor.SearchInteractor
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.PhotosCallback

class SearchViewModel(private val searchInteractor: SearchInteractor, private val dbInteractor: DataBaseInteractor) : ViewModel() {

    companion object {
        const val FIRST_PAGE = 1
        const val SECOND_PAGE = 2
    }

    private val disposables = CompositeDisposable()
    private var currentQuery = ""
    private val photosLiveData = MutableLiveData<PhotosCallback>()
    private var pageNumber = 1
    private var totalPages = 10
    private var dataList: MutableList<Photo> = mutableListOf()
    private var isFirstQuery = true


    fun getPhotosLiveData(): LiveData<PhotosCallback> = photosLiveData


    fun loading(perPage: Int) {
        if (pageNumber >= totalPages) {
            photosLiveData.value = PhotosCallback.PhotosEmpty
            return
        }

        searchInteractor.getPhotos(currentQuery, pageNumber, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    for (photo in photoPage.photosList) {
                        dataList.add(photo)
                        dbInteractor.addToCacheBase(photo)
                                .subscribeOn(Schedulers.io())
                                .subscribe({}, {th -> Logi.logIt("${th.message} in loading() -> addToCacheBase()")})
                    }
                    photosLiveData.value = PhotosCallback.PhotosLoaded(dataList)
                    pageNumber++
                }, { throwable: Throwable -> Logi.logIt("${throwable.message} in loading() -> getPhotos()")  })
                .apply { disposables.add(this) }
    }

    fun initLoad(query: String, perPage: Int) {
//        dbInteractor.clearCacheBase()
        if (!isFirstQuery) { return }
        isFirstQuery = false
        currentQuery = query
        disposables.add(
                searchInteractor.getPhotos(currentQuery, FIRST_PAGE, perPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ photoPage: PhotoPage ->
                            totalPages = photoPage.pages
                            for (photo in photoPage.photosList) {
                                dataList.add(photo) //добавление в текущий лист: List<Photo>, без PhotoSizes
                                dbInteractor.addToCacheBase(photo) // добавление в базу (кэш)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe({}, {th -> Logi.logIt("${th.message} in initLoad() -> addToCacheBase()")})
                            }
                            photosLiveData.value = PhotosCallback.PhotosLoaded(dataList)
                            pageNumber = SECOND_PAGE
                        }
                        ) { throwable: Throwable ->Logi.logIt("${throwable.message} in initLoad() -> getPhotos()") }
        )
    }



    fun clearDataList() {
        pageNumber = FIRST_PAGE
        if (dataList.isNotEmpty()) {
            dataList.clear()
//            dbInteractor.clearCacheBase()
        }
        isFirstQuery = true
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        dbInteractor.clearCacheBase()
    }
}