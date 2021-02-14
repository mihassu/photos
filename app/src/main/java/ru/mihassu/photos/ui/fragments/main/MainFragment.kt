package ru.mihassu.photos.ui.fragments.main

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.mihassu.photos.R
import ru.mihassu.photos.ui.helper.setupWithNavController

class MainFragment : Fragment() {

//    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var fm: FragmentManager
    private lateinit var viewModel: MainViewModel

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
        initViews(view)
        if (savedInstanceState == null) {
//            setupBottomNavigationBar()
        }
        fm = requireActivity().supportFragmentManager
        viewModel = ViewModelProvider(this, MainViewModelFactory(fm)).get(MainViewModel::class.java)
        viewModel.getActiveLiveData().observe(viewLifecycleOwner) { active ->
            Handler().post {
                active.commitNow()
                viewModel.showEnterAnimation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initFragments()
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
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.photos -> {
                    viewModel.changeFragment("photosFragment")
                    true
                }

                R.id.interest -> {
                    viewModel.changeFragment("interestFragment")
                    true
                }

                R.id.search -> {
                    viewModel.changeFragment("searchFragment")
                    true
                }

                R.id.favorites -> {
                    viewModel.changeFragment("favoritesFragment")
                    true
                }

                else -> false

            }
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

    override fun onDestroy() {
        super.onDestroy()
    }
}