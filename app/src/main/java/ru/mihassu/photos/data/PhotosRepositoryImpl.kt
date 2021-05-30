package ru.mihassu.photos.data

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.data.entity.info.PhotoInfoResponse
import ru.mihassu.photos.domain.*
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor

class PhotosRepositoryImpl(private val photosDataSource: PhotosDataSource, private val dataBaseInteractor: DataBaseInteractor) : PhotosRepository {

    override fun getRecentPhotos(pageNumber: Int, perPage: Int): Single<PhotoPage> {
        return photosDataSource.getRecent(pageNumber, perPage)
    }

    override fun searchPhotos(query: String, pageNumber: Int, perPage: Int): Single<PhotoPage> {
        return photosDataSource.search(query, pageNumber, perPage)
    }

    override fun getPhotoById(photoId: Long): Single<Photo> {
        return dataBaseInteractor.getFromCacheBaseById(photoId)
                .zipWith(photosDataSource.getPhotoSizes(photoId.toString()),  { photo, photoSizes ->
                    Logi.logIt("photoId: $photoId")
                    photo.copy(sizes = photoSizes) //добавление PhotoSizes
                })
                .zipWith(photosDataSource.getPhotoComments(photoId.toString()), { photo, photoComments ->
                    photo.copy(comments = photoComments) //добавление комментариев
                })
                .zipWith(photosDataSource.getPhotoInfo(photoId.toString()), { photo, photoInfoResponse ->
                    photo.copy(
                            views = photoInfoResponse.photo.views, // добавление Views
                            owner = photoInfoResponse.photo.owner.run {
                                PhotoOwner(username = username, realname = realname, location = location) // добавление PhotoOwner

                        }, tags = photoInfoResponse.photo.tags.run {
                                val tagsList = mutableListOf<PhotoTag>()
                                tag.forEach {
                                    tagsList.add(PhotoTag(id = it.id, author = it.author, content = it.content)) // добавление Tags
                                }
                                return@run tagsList
                        }, dates = photoInfoResponse.photo.dates.run { // добавление Dates
                                PhotoDates(
                                        posted = posted,
                                        taken = taken,
                                        takengranularity = takengranularity,
                                        takenunknown = takenunknown,
                                        lastupdate = lastupdate,
                                )
                        }
                    )

                })
    }

//    override fun getPhotoSizes(photoId: String): Single<List<PhotoSize>> {
//        return photosDataSource.getPhotoSizes(photoId)
//    }
//
//    override fun getPhotoComments(photoId: String): Single<List<PhotoComment>> {
//        return photosDataSource.getPhotoComments(photoId)
//    }

    override fun getInterestingPhotos(date: String, pageNumber: Int, perPage: Int): Single<PhotoPage> {
        return photosDataSource.getInterestingPhotos(date, pageNumber, perPage)
    }

    override fun toggleFavoritePhoto(photo: Photo) : Single<Boolean> {
        return dataBaseInteractor.toggleFavoritesInBase(photo)
    }

    override fun checkFavoritePhoto(photo: Photo): Single<Boolean> {
        return dataBaseInteractor.isPhotoFavorite(photo)
    }
}