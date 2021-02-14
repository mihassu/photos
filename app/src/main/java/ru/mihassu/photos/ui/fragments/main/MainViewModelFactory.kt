package ru.mihassu.photos.ui.fragments.main

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mihassu.photos.repository.PhotosRepository
import ru.mihassu.photos.ui.db.DataBaseInteractor

class MainViewModelFactory(private val fragmentManager: FragmentManager)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            MainViewModel::class.java -> MainViewModel(fragmentManager) as T
            else -> null
        }!!
    }
}