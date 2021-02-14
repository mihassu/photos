package ru.mihassu.photos.ui.fragments.interest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.PhotoPage
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.common.BaseViewModel
import ru.mihassu.photos.ui.fragments.common.PhotosCallback
import java.util.*

class InterestViewModel(private val photosRepository: PhotosRepository, dbInteractor: DataBaseInteractor) : BaseViewModel(dbInteractor) {

    private val PER_PAGE = 40

    private val datesLiveData: MutableLiveData<DatesListState> = MutableLiveData()
    private val datesListState: DatesListState = DatesListState()

    init {
//        datesLiveData.value = datesListState.setCurrentDate(0)
//        initLoad(datesListState.getCurrentDatePos())
    }

    fun getDatesLiveData() : LiveData<DatesListState> = datesLiveData


    fun loading() {
        if (pageNumber >= totalPages) {
            photosLiveData.value = PhotosCallback.PhotosEmpty(dataListState.getDataList())
            return
        }
        photosRepository.getInterestingPhotos(datesListState.getCurrentDate(), pageNumber, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    dataListState.updateDataList(photoPage.photosList)
                    pageNumber++
                }, { throwable: Throwable -> Logi.logIt("${throwable.message} in loading() -> getInterestingPhotos()")  })
                .apply { disposables.add(this) }
    }

    fun initLoad(datePos: Int) {
        if (!isFirstQuery) {
            photosLiveData.value = PhotosCallback.PhotosLoaded(dataListState.getDataList())
            return
        }
        isFirstQuery = false

        datesListState.setCurrentDate(datePos)

        photosRepository.getInterestingPhotos(datesListState.getCurrentDate(), FIRST_PAGE, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photoPage: PhotoPage ->
                    totalPages = photoPage.pages
                    dataListState.updateDataList(photoPage.photosList)
                    datesLiveData.value = datesListState
                    pageNumber = SECOND_PAGE
                }
                ) { throwable: Throwable -> Logi.logIt("${throwable.message} in initLoad() -> getInterestingPhotos()") }
                .apply { disposables.add(this) }

    }

//    fun setDate(datePos: Int) {
//        datesLiveData.value = datesListState.setCurrentDate(datePos)
//    }


    override fun onRefresh() {
        super.onRefresh()
    }
}