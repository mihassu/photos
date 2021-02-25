package ru.mihassu.photos.ui.fragments.main

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.mihassu.photos.R
import ru.mihassu.photos.ui.MainActivity
import ru.mihassu.photos.ui.fragments.common.AnimatedFragment
import ru.mihassu.photos.ui.fragments.favorite.FavoriteFragment
import ru.mihassu.photos.ui.fragments.interest.InterestFragment
import ru.mihassu.photos.ui.fragments.photos.PhotosFragment
import ru.mihassu.photos.ui.fragments.search.SearchFragment

class MainViewModel : ViewModel() {

    private val activeLiveData = MutableLiveData<Pair<Fragment, String>>()
    private var isFirstStart: Boolean = true

    init {
        activeLiveData.value = PhotosFragment.getInstance() to MainFragment.PHOTOS_TAG
    }

    fun getActiveLiveData() : LiveData<Pair<Fragment, String>> = activeLiveData

    fun setActiveFragment(fragment: Fragment, tag: String) {
        activeLiveData.value = fragment to tag
    }


    override fun onCleared() {
        super.onCleared()
    }
}