package ru.mihassu.photos.ui.db

import io.reactivex.Completable
import io.reactivex.Single
import ru.mihassu.photos.domain.Photo

interface DataBaseInteractor {

    fun addToFavoritesBase(photo: Photo) : Single<Boolean>
//    fun deleteFromFavoritesBase(photo: Photo) : Completable
    fun getAllFromFavoritesBase() : Single<List<Photo>>
    fun isPhotoFavorite(photo: Photo): Single<Boolean>

    fun addToCacheBase(photo: Photo) : Completable
    fun getFromCacheBaseById(photoId: Long) : Single<Photo>
    fun clearCacheBase() : Completable
}