package ru.mihassu.photos.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {


//    private var activeFragment: Fragment? = null
    private val activeLiveData = MutableLiveData<Fragment>()
    private var isFirstStart = true

    fun getActiveLiveData() : LiveData<Fragment> = activeLiveData


    fun setActiveFragment(fragment: Fragment) {
        activeLiveData.value = fragment
    }

    override fun onCleared() {
        super.onCleared()
//        activeFragment = null
    }
}