package ru.mihassu.photos.db

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.domain.Photo
import ru.mihassu.photos.domain.PhotoSize
import ru.mihassu.photos.ui.db.DataBaseInteractor
import java.util.*

class DataBaseInteractorImpl(db: AppDataBase) : DataBaseInteractor {

    private val favoriteDao: FavoritePhotosDao = db.favoritePhotosDao()
    private val cacheDao: CachePhotosDao = db.cachePhotosDao()
    private val photoSizesDao: PhotoSizesDao = db.photoSizesDao()

//    init {
//        Logi.logIt("DATABASE VERSION ${db.}")
//    }

    /*FavoritesBase*/
    override fun addToFavoritesBase(photo: Photo): Single<Boolean> = Single.create { emitter ->
        if (!isContainPhoto(photo)) {
            try {
                favoriteDao.insert(photo.toDbEntity())
                photoSizesDao.insert(photo.sizes.map { it.toDbEntity(photo.id) })
            } catch (th: Throwable) {
                emitter.onError(th)
            }
            emitter.onSuccess(true)
        } else {
            try {
                favoriteDao.delete(photo.toDbEntity())
                photoSizesDao.delete(photo.sizes.map { it.toDbEntity(photo.id) })
            } catch (th: Throwable) {
                emitter.onError(th)
            }
            emitter.onSuccess(false)
        }
    }

    private fun isContainPhoto(photo: Photo) : Boolean {
        val photos = favoriteDao.selectPhotoById(photo.id)
        return photos.isNotEmpty()
    }

    override fun isPhotoFavorite(photo: Photo) : Single<Boolean> = Single.create { emitter ->
        emitter.onSuccess(isContainPhoto(photo))
    }

    override fun getAllFromFavoritesBase(): Single<List<Photo>> = Single.create { emitter ->
        val photosList = mutableListOf<Photo>()
        try {
            val dbPhotosList = favoriteDao.selectAll()
            for (dbPhoto in dbPhotosList) {
                var photo = dbPhoto.toEntity()
                val sizesList = photoSizesDao.selectPhotoSizesById(photo.id)
                photo = photo.copy(sizes = sizesList.map { it.toEntity() })
                photosList.add(photo)
            }
        } catch (th: Throwable) {
            emitter.onError(th)
        }
        emitter.onSuccess(photosList)
    }

    private fun FavoritePhotosEntity.toEntity() : Photo {
        return Photo(this.id, this.title, this.url)
    }

    private fun Photo.toDbEntity() : FavoritePhotosEntity {
        return FavoritePhotosEntity(this.id, this.title, this.url)
    }

    /*CacheBase*/
    override fun addToCacheBase(photo: Photo): Completable = Completable.create { emitter ->
        if (!isContainPhotoCache(photo)) {
            try {
                cacheDao.insert(photo.toCacheBaseEntity())
            } catch (th: Throwable) {
                emitter.onError(th)
            }
        }
        emitter.onComplete()
    }

    override fun getFromCacheBaseById(photoId: Long): Single<Photo> = Single.create { emitter ->
        var photo: Photo? = null
        try {
            photo = cacheDao.selectPhotoById(photoId)[0].toEntity()
        } catch (th: Throwable) {
            emitter.onError(th)
        }
        photo?.let { emitter.onSuccess(it) }
//                ?: let { emitter.onError(Throwable("No photo")) }

    }

    override fun clearCacheBase(): Completable = Completable.create { emitter ->
        try {
            cacheDao.clear()
        } catch (th: Throwable) {
            emitter.onError(th)
        }
        emitter.onComplete()
    }

    private fun isContainPhotoCache(photo: Photo) : Boolean {
        val photos = cacheDao.selectPhotoById(photo.id)
        return photos.isNotEmpty()
    }


    private fun CachePhotosEntity.toEntity() : Photo {
        return Photo(this.id, this.title, this.url)
    }

    private fun Photo.toCacheBaseEntity() : CachePhotosEntity {
        return CachePhotosEntity(this.id, this.title, this.url)
    }

    /*PhotoSizesBase*/
    private fun PhotoSize.toDbEntity(photoId: Long): PhotoSizesEntity {
        return PhotoSizesEntity(UUID.randomUUID().toString(), photoId, this.label, this.width, this.height, this.source, this.url, this.media)
    }

    private fun PhotoSizesEntity.toEntity() : PhotoSize {
        return PhotoSize(this.label, this.width, this.height, this.source, this.url, this.media)
    }
}