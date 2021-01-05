package ru.mihassu.photos.ui.fragments.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor

class SinglePhotoViewModelFactory(private val photosRepository: PhotosRepository, private val dbInteractor: DataBaseInteractor)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            SinglePhotoViewModel::class.java -> SinglePhotoViewModel(photosRepository, dbInteractor) as T
            else -> null
        }!!
    }
}