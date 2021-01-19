package ru.mihassu.photos.ui.fragments.interest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import ru.mihassu.photos.interactor.SearchInteractor
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor
import ru.mihassu.photos.ui.fragments.search.SearchViewModel

class InterestViewModelFactory(private val photosRepository: PhotosRepository, private val dataBaseInteractor: DataBaseInteractor) : NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return when (modelClass) {
            InterestViewModel::class.java -> InterestViewModel(photosRepository, dataBaseInteractor) as T
            else -> null
        }!!
    }
}