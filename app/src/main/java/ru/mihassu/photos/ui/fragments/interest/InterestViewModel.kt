package ru.mihassu.photos.ui.fragments.interest

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.PhotoPage
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.ViewModelBase
import ru.mihassu.photos.ui.fragments.common.PhotosCallback

class InterestViewModel(private val photosRepository: PhotosRepository, dbInteractor: DataBaseInteractor) : ViewModelBase(dbInteractor) {

    fun loading(date: String, perPage: Int) {
        if (pageNumber >= totalPages) {
            photosLiveData.value = PhotosCallback.PhotosEmpty(dataListState.getDataList())
            return
        }
        photosRepository.getInterestingPhotos(date, pageNumber, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    dataListState.updateDataList(photoPage.photosList)
                    pageNumber++
                }, { throwable: Throwable -> Logi.logIt("${throwable.message} in loading() -> getInterestingPhotos()")  })
                .apply { disposables.add(this) }
    }

    fun initLoad(date: String, perPage: Int) {
        if (!isFirstQuery) { return }
        isFirstQuery = false

        photosRepository.getInterestingPhotos(date, FIRST_PAGE, perPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    totalPages = photoPage.pages
                    dataListState.updateDataList(photoPage.photosList)
                    pageNumber = SECOND_PAGE
                }
                ) { throwable: Throwable -> Logi.logIt("${throwable.message} in initLoad() -> getInterestingPhotos()") }
                .apply { disposables.add(this) }

    }
}