package ru.mihassu.photos.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.mihassu.photos.R
import ru.mihassu.photos.ui.MainActivity
import ru.mihassu.photos.ui.helper.setupWithNavController

class MainFragment : Fragment() {

//    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentNavController: LiveData<NavController>? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)
        initViews(v)
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_bottom)
//        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupBottomNavigationBar()
    }


    private fun initViews(v: View) {
        bottomNavigationView = v.findViewById(R.id.bottom_navigation)
//        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//            when(item.itemId) {
//                R.id.photos_fragment_nav -> {
//                    if (navController.currentDestination?.id != R.id.photos_fragment_nav) {
//                        navController.navigate(R.id.action_favorite_to_photos)
//                        true
//                    } else false
//                }
//                R.id.favorite_fragment_nav -> {
//                    if (navController.currentDestination?.id != R.id.favorite_fragment_nav) {
//                        navController.navigate(R.id.action_photos_to_favorite)
//                        true
//                    } else false
//                }
//                else -> false
//            }
//        }
    }

    private fun setupBottomNavigationBar() {
        val navGraphsIds: List<Int> = listOf(R.navigation.photos_navigation, R.navigation.search_navigation, R.navigation.favorites_navigation)
        val controllerLiveData = bottomNavigationView.setupWithNavController(
                navGraphsIds,
                requireActivity().supportFragmentManager,
                R.id.nav_host_container,
                requireActivity().intent
        )

        controllerLiveData.observe(viewLifecycleOwner, Observer { controller ->
//            NavigationUI.setupActionBarWithNavController(MainActivity.activityComponent.activity, controller)
//            NavigationUI.setupWithNavController(bottomNavigationView, controller)
            Navigation.setViewNavController(requireView(), controller)
        })
        currentNavController = controllerLiveData
    }



    fun hideBottomNavigation() {
        
    }
}