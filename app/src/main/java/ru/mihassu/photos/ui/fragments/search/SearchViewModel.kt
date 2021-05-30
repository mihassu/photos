package ru.mihassu.photos.ui.fragments.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.PhotoPage
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.base.BaseViewModel
import ru.mihassu.photos.ui.fragments.common.PhotosCallback

class SearchViewModel(private val photosRepository: PhotosRepository, dbInteractor: DataBaseInteractor) : BaseViewModel(dbInteractor) {

    private var currentQuery = ""

    fun loading(perPage: Int) {
        if (pageNumber >= totalPages) {
            photosLiveData.value = PhotosCallback.PhotosEmpty(dataListState.getDataList())
            return
        }

        photosRepository.searchPhotos(currentQuery, pageNumber, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    dataListState.updateDataList(photoPage.photosList)
                    pageNumber++
                }, { throwable: Throwable -> Logi.logIt("${throwable.message} in loading() -> searchPhotos()")  })
                .apply { disposables.add(this) }
    }

    fun initLoad(query: String, perPage: Int) {
        if (!isFirstQuery) { return }
        isFirstQuery = false
        currentQuery = query
        disposables.add(
                photosRepository.searchPhotos(currentQuery, FIRST_PAGE, perPage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ photoPage: PhotoPage ->
                            totalPages = photoPage.pages
                            dataListState.updateDataList(photoPage.photosList)
                            pageNumber = SECOND_PAGE
                        }
                        ) { throwable: Throwable ->Logi.logIt("${throwable.message} in initLoad() -> searchPhotos()") }
        )
    }
}