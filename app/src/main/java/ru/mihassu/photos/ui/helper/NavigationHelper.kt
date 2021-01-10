package ru.mihassu.photos.ui.helper

import android.content.Intent
import android.os.Handler
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.mihassu.photos.R


fun BottomNavigationView.setupWithNavController(navGraphIds: List<Int>, fm: FragmentManager, containerId: Int, intent: Intent): LiveData<NavController> {

    val graphIdToTagMap = SparseArray<String>()
    val selectedNavControllerLiveData = MutableLiveData<NavController>() // NavController выбранной вкладки
    var firstFragmentGraphId = 0

    // Создание NavHostFragment для каждого графа
    navGraphIds.forEachIndexed { index, navGraphId ->

        val fragmentTag = "bottomNavigation$index"

        // Получить NavHostFragment для графа
        val navHostFragment = obtainNavHostFragment(fm, fragmentTag, navGraphId, containerId)

        val graphId = navHostFragment.navController.graph.id
        if (index == 0) {
            firstFragmentGraphId = graphId
        }

        // Добавить граф в таблицу
        graphIdToTagMap[graphId] = fragmentTag

        // Отсоединить или присоединить NavHostFragment в зависимости от того выбран он или нет
        if (this.selectedItemId == graphId) {
            selectedNavControllerLiveData.value = navHostFragment.navController
            attachNavHostFragment(fm, navHostFragment, index == 0)
        } else {
            detachNavHostFragment(fm, navHostFragment)
        }
    }

    var selectedItemTag = graphIdToTagMap[this.selectedItemId] // взять выбранный фрагмент из таблицы
    val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId] // взять первый фрагмент из таблицы
    var isOnFirstFragment = selectedItemTag == firstFragmentTag // выбран ли первый фрагмент

    setOnNavigationItemSelectedListener { item ->
        if (fm.isStateSaved) {
            false
        } else {
            val newSelectedItemTag = graphIdToTagMap[item.itemId] // тэг фрагмента, на который кликнули
            if (selectedItemTag != newSelectedItemTag) { // если клик не по текущему фрагменту
                // достать из backStack все что выше первого фрагмента
                fm.popBackStack(firstFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                val selectedFragment = fm.findFragmentByTag(newSelectedItemTag) as NavHostFragment

                if (firstFragmentTag != newSelectedItemTag) { // если клик не по первому фрагменту
                    fm.beginTransaction()
                            .setCustomAnimations(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim, R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim)
                            .attach(selectedFragment) // прикрепить фрагмент выбранной вкладки
                            .setPrimaryNavigationFragment(selectedFragment)
                            .apply {
                                graphIdToTagMap.forEach { _, fragmentTag ->
                                    if (fragmentTag != newSelectedItemTag) {
                                        detach(fm.findFragmentByTag(firstFragmentTag)!!) // отсоединить невыбранные фрагменты
                                    }
                                }
                            }
                            .addToBackStack(firstFragmentTag)
                            .setReorderingAllowed(true)
                            .commit()
                }
                selectedItemTag = newSelectedItemTag
                isOnFirstFragment = selectedItemTag == firstFragmentTag
                selectedNavControllerLiveData.value = selectedFragment.navController
                true
            } else {
                false
            }
        }
    }

    setupItemReselected(graphIdToTagMap, fm)

    // Finally, ensure that we update our BottomNavigationView when the back stack changes
    fm.addOnBackStackChangedListener {
        if (!isOnFirstFragment && !fm.isOnBackStack(firstFragmentTag)) {
            this.selectedItemId = firstFragmentGraphId
        }
    }

    /*Reset the graph if the currentDestination is not valid (happens when the back
    /stack is popped after using the back button)*/
    selectedNavControllerLiveData.value?.let { controller ->
        if (controller.currentDestination == null) {
            controller.navigate(controller.graph.id)
        }
    }

    return selectedNavControllerLiveData
}

// Создание NavHostFragment-a для графа навигации вкладки
fun obtainNavHostFragment(fm: FragmentManager, fragmentTag: String, navGraphId: Int, containerId: Int) : NavHostFragment{

    //Если NavHostFragment уже есть
    val existingNavHost = fm.findFragmentByTag(fragmentTag) as? NavHostFragment
    existingNavHost?.let { return it }

    //Если нет - создать NavHostFragment
    val navHostFragment = NavHostFragment.create(navGraphId)
    fm.beginTransaction()
            .add(containerId, navHostFragment, fragmentTag)
            .commitNow()
    return navHostFragment
}

private fun attachNavHostFragment(fm: FragmentManager, navHostFragment: NavHostFragment, isPrimaryNavHostFragment: Boolean) {
    Handler().post {
        fm.beginTransaction()
                .attach(navHostFragment).apply {
                    if (isPrimaryNavHostFragment) {
                        setPrimaryNavigationFragment(navHostFragment)
                    }
                }
                .commitNow()
    }
}

private fun detachNavHostFragment(fm: FragmentManager, navHostFragment: NavHostFragment) {
    Handler().post {
        fm.beginTransaction().detach(navHostFragment).commitNow()
    }
}

private fun BottomNavigationView.setupItemReselected(graphIdToTagMap: SparseArray<String>, fm: FragmentManager) {
    setOnNavigationItemReselectedListener { item ->
        val newSelectedItemTag = graphIdToTagMap[item.itemId]
        val selectedFragment = fm.findFragmentByTag(newSelectedItemTag) as NavHostFragment
        val navController = selectedFragment.navController

        // Pop the back stack to the start destination of the current navController graph
        navController.popBackStack(navController.graph.startDestination, false)
    }
}

private fun FragmentManager.isOnBackStack(backStackName: String) : Boolean {
    val backStackCount = backStackEntryCount
    for (i in 0 until backStackCount) {
        if (getBackStackEntryAt(i).name == backStackName) {
            return true
        }
    }
    return false
}

