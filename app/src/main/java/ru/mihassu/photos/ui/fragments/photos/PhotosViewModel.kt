package ru.mihassu.photos.ui.fragments.photos

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.PhotoPage
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.BaseViewModel
import ru.mihassu.photos.ui.fragments.common.PhotosCallback

class PhotosViewModel(private val photosRepository: PhotosRepository, dbInteractor: DataBaseInteractor) : BaseViewModel(dbInteractor) {

    fun loading(perPage: Int) {
        if (pageNumber >= totalPages) {
            photosLiveData.value = PhotosCallback.PhotosEmpty(dataListState.getDataList())
            return
        }

        photosRepository.getRecentPhotos(pageNumber, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    dataListState.updateDataList(photoPage.photosList)
                    pageNumber++
                }, { throwable: Throwable -> Logi.logIt("${throwable.message} in loading() -> getPhotos()")  })
                .apply { disposables.add(this) }
    }

    fun initLoad(perPage: Int) {
        if (!isFirstQuery) {
            photosLiveData.value = PhotosCallback.PhotosLoaded(dataListState.getDataList())
            return
        }
        isFirstQuery = false
//        currentQuery = query

        photosRepository.getRecentPhotos(FIRST_PAGE, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    totalPages = photoPage.pages
                    dataListState.updateDataList(photoPage.photosList)
                    pageNumber = SECOND_PAGE
                }
                ) { throwable: Throwable ->Logi.logIt("${throwable.message} in initLoad() -> getPhotos()") }
                .apply { disposables.add(this) }

    }
}