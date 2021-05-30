package ru.mihassu.photos.ui.fragments.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor

class SinglePhotoViewModelFactory(private val photosRepository: PhotosRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            SinglePhotoViewModel::class.java -> SinglePhotoViewModel(photosRepository) as T
            else -> null
        }!!
    }
}