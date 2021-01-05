package ru.mihassu.photos.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.mihassu.photos.R

class MainFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

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
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_bottom)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
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

    fun hideBottomNavigation() {
        
    }
}