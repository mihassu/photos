package ru.mihassu.photos.ui.fragments.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.Completable
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.ui.MainActivity
import ru.mihassu.photos.ui.fragments.FragmentsNavigation
import ru.mihassu.photos.ui.fragments.common.AnimatedFragment
import ru.mihassu.photos.ui.fragments.favorite.FavoriteFragment
import ru.mihassu.photos.ui.fragments.interest.InterestFragment
import ru.mihassu.photos.ui.fragments.photos.PhotosFragment
import ru.mihassu.photos.ui.fragments.search.SearchFragment
import ru.mihassu.photos.ui.helper.setupWithNavController

class MainFragment : Fragment() {

    companion object {
        const val PHOTOS_TAG = "photosFragment"
        const val INTEREST_TAG = "interestFragment"
        const val SEARCH_TAG = "searchFragment"
        const val FAVORITES_TAG = "favoritesFragment"
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private var activeFragment: Fragment? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        fragmentsNavigation = context as FragmentsNavigation

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_bottom)
//        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = NavHostFragment.findNavController(this)
        initViews(view)
        if (savedInstanceState == null) {
//            setupBottomNavigationBar()
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getActiveLiveData().observe(viewLifecycleOwner) { active ->
            replaceFragment(active.first, active.second)
        }
    }

    override fun onResume() {
        super.onResume()
//        viewModel.initFragments()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
//        setupBottomNavigationBar()
    }

    override fun onPause() {
        super.onPause()
//        viewModel.detachActive()
    }


    private fun initViews(v: View) {
        bottomNavigationView = v.findViewById(R.id.bottom_navigation)
//        navController = Navigation.findNavController(v)
//        NavigationUI.setupWithNavController(bottomNavigationView, navController)
//        bottomNavigationView.setupWithNavController(navController)
//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            Logi.logIt("Current destination: ${navController.currentDestination.toString()}")
//            navController.navigate(item.itemId)
//            return@setOnNavigationItemSelectedListener true
//        }


        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.navigation_photos_fragment -> { viewModel.setActiveFragment(PhotosFragment.getInstance(), PHOTOS_TAG); true }
                R.id.navigation_interest_fragment -> { viewModel.setActiveFragment(InterestFragment.getInstance(), INTEREST_TAG); true }
                R.id.navigation_search_fragment -> { viewModel.setActiveFragment(SearchFragment.getInstance(), SEARCH_TAG); true }
                R.id.navigation_favorites_fragment -> { viewModel.setActiveFragment(FavoriteFragment.getInstance(), FAVORITES_TAG); true }
                else -> false

            }
        }
    }

    private fun replaceFragment(fragment: Fragment, fragmentTag: String) {

        requireActivity().supportFragmentManager.beginTransaction().run {
//            activeFragment?.let {
//                if (it == fragment) { detach(it); return@let }
//                (it as? AnimatedFragment)?.let { animatedFragment ->
//                    animatedFragment.showQuitAnimation().subscribe {
//                        detach(it)
//                    } }
//                        ?: detach(it)
//            }
            detachWithAnimation(this, fragment).subscribe {
                activeFragment = fragment
                if (requireActivity().supportFragmentManager.findFragmentByTag(fragmentTag) == null) {
                    add(R.id.nav_container_bottom, activeFragment!!, fragmentTag)
                }
                attach(activeFragment!!)
                commit()
            }
        }
    }

    private fun detachWithAnimation(ft: FragmentTransaction, fragment: Fragment): Completable {
        return Completable.create { emitter ->
            activeFragment?.let {
                if (it == fragment) { ft.detach(it); emitter.onComplete() }
                (it as? AnimatedFragment)?.let { animatedFragment ->
                    animatedFragment.showQuitAnimation().subscribe({
                        ft.detach(it)
                        emitter.onComplete()
                    }, { th ->
                        Logi.logIt("ERROR in MainFragment - detachWithAnimation(): ${th.message}")
                        ft.detach(it)
                        emitter.onComplete()
                    })}
                        ?: run { ft.detach(it); emitter.onComplete() }
            } ?:
            emitter.onComplete()
        }
    }

    private fun setupBottomNavigationBar() {
        val navGraphsIds: List<Int> = listOf(
                R.navigation.photos_navigation,
                R.navigation.interest_navigation,
                R.navigation.search_navigation,
                R.navigation.favorites_navigation
        )
        val controllerLiveData = bottomNavigationView.setupWithNavController(
                navGraphsIds,
                requireActivity().supportFragmentManager,
                R.id.nav_container_bottom,
                requireActivity().intent
        )

        controllerLiveData.observe(viewLifecycleOwner, Observer { controller ->
//            NavigationUI.setupActionBarWithNavController(MainActivity.activityComponent.activity, controller)
//            NavigationUI.setupWithNavController(bottomNavigationView, controller)
            Navigation.setViewNavController(requireView(), controller)
        })
        currentNavController = controllerLiveData
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}