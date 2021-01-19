package ru.mihassu.photos.ui.fragments.common

import ru.mihassu.photos.domain.Photo

sealed class PhotosCallback {
    class PhotosLoaded(val photos: List<Photo>) : PhotosCallback()
//    object FavoriteAdded : PhotosCallback()
//    object FavoriteDeleted : PhotosCallback()
    class PhotosError(val th: Throwable) : PhotosCallback()
    class PhotosEmpty(val photos: List<Photo>) : PhotosCallback()
}