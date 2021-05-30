package ru.mihassu.photos.ui.fragments.photo

import ru.mihassu.photos.domain.Photo

sealed class SinglePhotoCallback {
    class PhotoLoaded(val photo: Photo): SinglePhotoCallback()
    class PhotoError(val error: Throwable): SinglePhotoCallback()
}