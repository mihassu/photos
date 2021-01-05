package ru.mihassu.photos.ui.fragments.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.ui.db.DataBaseInteractor

class FavoriteViewModel(private val dbInteractor: DataBaseInteractor) : ViewModel() {

    private val favoritesLiveData = MutableLiveData<List<Photo>>()
    private val disposables = CompositeDisposable()
    private var dataList: List<Photo> = listOf()



    fun getPhotosLiveData(): LiveData<List<Photo>> = favoritesLiveData

    fun getFavorites() {
        dbInteractor.getAllFromFavoritesBase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ photos ->
                    dataList = photos
                    favoritesLiveData.value = dataList
                },
                        {th -> }).apply { disposables.add(this) }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}