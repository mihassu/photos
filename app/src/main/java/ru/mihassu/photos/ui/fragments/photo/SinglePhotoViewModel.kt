package ru.mihassu.photos.ui.fragments.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.domain.PhotoSize
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor

class SinglePhotoViewModel(private val photosRepository: PhotosRepository, private val dbInteractor: DataBaseInteractor) : ViewModel() {

    private val photoLiveData: MutableLiveData<Photo> = MutableLiveData<Photo>()
    private val isPhotoFavoriteLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val disposables: CompositeDisposable = CompositeDisposable()


    fun getIsPhotoFavoriteLiveData(): LiveData<Boolean> = isPhotoFavoriteLiveData
    fun getPhotoLiveData() : LiveData<Photo> = photoLiveData

    fun load(photoId: Long) {
        dbInteractor.getFromCacheBaseById(photoId)
                .flatMap { photo -> photosRepository.getPhotoSizes(photo.id.toString())}
                .zipWith(dbInteractor.getFromCacheBaseById(photoId), object : BiFunction<List<PhotoSize>, Photo, Photo> {
                    override fun apply(photoSizes: List<PhotoSize>, photo: Photo): Photo {
                        return photo.copy(sizes = photoSizes) //добавление PhotoSizes
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ photo -> photoLiveData.value = photo}, { th -> Logi.logIt("SinglePhotoViewModel - load() error: ${th.message}")})
                .apply { disposables.add(this) }
    }

    fun addFavoritePhoto(photo: Photo) {
        dbInteractor.addToFavoritesBase(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isFavorite ->
                    isPhotoFavoriteLiveData.value = isFavorite
                }, { th -> Logi.logIt("SinglePhotoViewModel - addToFavoritesBase() error: ${th.message}")} )
                .apply { disposables.add(this) }
    }

    fun checkFavorite(photo: Photo) {
        dbInteractor.isPhotoFavorite(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isFavorite -> isPhotoFavoriteLiveData.value = isFavorite }
                .apply { disposables.add(this) }

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}