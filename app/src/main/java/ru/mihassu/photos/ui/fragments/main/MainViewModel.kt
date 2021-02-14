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

class MainViewModel(private val fragmentManager: FragmentManager) : ViewModel() {

    private var photosFragment: Fragment? = PhotosFragment()
    private var interestFragment: Fragment?  = InterestFragment()
    private var searchFragment: Fragment?  = SearchFragment()
    private var favoritesFragment: Fragment?  = FavoriteFragment()
    private var activeFragment: Fragment? = photosFragment
    private val activeLiveData = MutableLiveData<FragmentTransaction>()


    fun getActiveLiveData() : LiveData<FragmentTransaction> = activeLiveData


    fun initFragments() {

        fragmentManager.beginTransaction().apply {
            if (fragmentManager.findFragmentByTag("photosFragment") == null) {
                add(R.id.nav_host_container, favoritesFragment!!, "favoritesFragment").detach(favoritesFragment!!)
                add(R.id.nav_host_container, searchFragment!!, "searchFragment").detach(searchFragment!!)
                add(R.id.nav_host_container, interestFragment!!, "interestFragment").detach(interestFragment!!)
                add(R.id.nav_host_container, photosFragment!!, "photosFragment").detach(photosFragment!!)
            }
            commit()
        }
        activeLiveData.value = fragmentManager.beginTransaction().apply {
            detach(activeFragment!!)
            attach(activeFragment!!)
        }
//                .setCustomAnimations(R.anim.show_anim, R.anim.hide_anim, R.anim.show_anim, R.anim.hide_anim)
    }

    fun detachActive() {
        (activeFragment as? AnimatedFragment)?.let { it.showQuitAnimation()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
            fragmentManager.beginTransaction().detach(activeFragment!!).commit()
        } } ?: fragmentManager.beginTransaction().detach(activeFragment!!).commit()
    }

    fun changeFragment(fragmentTag: String) {
        fragmentManager.findFragmentByTag(fragmentTag)?.let {
            (activeFragment as? AnimatedFragment)?.showQuitAnimation()?.subscribe()
            fragmentManager.beginTransaction().detach(activeFragment!!).commit()
            activeFragment = it
            activeLiveData.value = fragmentManager.beginTransaction().apply {
                detach(activeFragment!!)
                attach(activeFragment!!)
            }
        }

    }

    fun showEnterAnimation() {
        (activeFragment as? AnimatedFragment)?.showEnterAnimation()?.subscribe()
    }


    override fun onCleared() {
        super.onCleared()
        photosFragment = null
        interestFragment = null
        searchFragment = null
        favoritesFragment = null
        activeFragment = null
    }
}