package ru.mihassu.photos.ui.fragments.common

import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.ui.db.DataBaseInteractor

class DataListState (private val dbInteractor: DataBaseInteractor) {

    private val dataList: MutableList<Photo> = mutableListOf()
    val dataListProcessor: PublishSubject<MutableList<Photo>> = PublishSubject.create()
    private val disposables = CompositeDisposable()


    fun updateDataList(newList: List<Photo>) {
        for (photo in newList) {
            if (!isContainInDataList(photo)) {
                dataList.add(photo) //добавление в текущий лист: List<Photo>, без PhotoSizes
            }
        }
        dataListProcessor.onNext(dataList)
    }

    private fun isContainInDataList(photo: Photo) : Boolean {
        for (ph in dataList) {
            if (ph == photo) { return true }
        }
        return false
    }

    fun getDataList() = dataList

    fun addToCacheBase(photo: Photo): Completable {
        return dbInteractor.addToCacheBase(photo)
                .subscribeOn(Schedulers.io())
//                .subscribe({}, {th -> Logi.logIt("Add to cache ERROR: ${th.message}")})
//                .apply { disposables.add(this) }
    }

    private fun clearDataList() {
        if (dataList.isNotEmpty()) {
            dataList.clear()
        }
    }


    fun clear() {
        disposables.dispose()
        dbInteractor.clearCacheBase().subscribeOn(Schedulers.io()).subscribe()
        clearDataList()
    }
}