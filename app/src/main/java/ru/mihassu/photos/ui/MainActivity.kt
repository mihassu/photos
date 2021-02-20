package ru.mihassu.photos.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.disposables.CompositeDisposable
import okhttp3.*
import ru.mihassu.photos.R
import ru.mihassu.photos.common.Logi
import ru.mihassu.photos.di.ActivityComponent
import ru.mihassu.photos.di.ActivityContextModule
import ru.mihassu.photos.di.DaggerActivityComponent
import ru.mihassu.photos.ui.fragments.FragmentsNavigation
import ru.mihassu.photos.ui.fragments.common.AnimatedFragment
import ru.mihassu.photos.ui.fragments.favorite.FavoriteFragment
import ru.mihassu.photos.ui.fragments.interest.InterestFragment
import ru.mihassu.photos.ui.fragments.photos.PhotosFragment
import ru.mihassu.photos.ui.fragments.search.SearchFragment
import java.io.IOException

class MainActivity : AppCompatActivity(), FragmentsNavigation {

    private var photosFragment: Fragment? = null
    private var interestFragment: Fragment?  = null
    private var searchFragment: Fragment?  = null
    private var favoritesFragment: Fragment?  = null
    private var activeFragment: Fragment? = null
    private val disposables: CompositeDisposable = CompositeDisposable()

    private lateinit var viewModel: MainActivityViewModel
    private val searchButton: Button? = null

    companion object {
        @JvmStatic
        lateinit var activityComponent: ActivityComponent
        const val PHOTOS_TAG = "photosFragment"
        const val INTEREST_TAG = "interestFragment"
        const val SEARCH_TAG = "searchFragment"
        const val FAVORITES_TAG = "favoritesFragment"
    }

    //    //Если конструктор презентера с параметрами
    //    @ProvidePresenter
    //    MainPresenter provideMainPresenter() {
    //        return new MainPresenter(App.getAppComponent().getPhotosRepository());
    //    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        initFragments()
        setContentView(R.layout.activity_main)

        activityComponent = DaggerActivityComponent.builder()
                .activityContextModule(ActivityContextModule(this))
                .build()

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getActiveLiveData().observe(this, Observer { active ->
            changeFragment(active)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.setActiveFragment(activeFragment!!)
    }


    private fun initFragments() {
        photosFragment = PhotosFragment()
        interestFragment = InterestFragment()
        searchFragment = SearchFragment()
        favoritesFragment = FavoriteFragment()
        activeFragment = photosFragment

        supportFragmentManager.beginTransaction().apply {
            add(R.id.nav_host_container, favoritesFragment!!, FAVORITES_TAG).detach(favoritesFragment!!)
            add(R.id.nav_host_container, searchFragment!!, SEARCH_TAG).detach(searchFragment!!)
            add(R.id.nav_host_container, interestFragment!!, INTEREST_TAG).detach(interestFragment!!)
            add(R.id.nav_host_container, photosFragment!!, PHOTOS_TAG).detach(photosFragment!!)
            commit()
        }
    }

    private fun changeFragment(fragment: Fragment) {
            (fragment as? AnimatedFragment)?.showQuitAnimation()?.subscribe ({
                supportFragmentManager.beginTransaction().detach(activeFragment!!).commit()
                activeFragment = fragment
                supportFragmentManager.beginTransaction()
//                        .detach(activeFragment!!)
                        .attach(activeFragment!!)
                        .commit()
                showEnterAnimation()

            }, { throwable ->
                Logi.logIt("No quit animation. Error: $throwable")
                supportFragmentManager.beginTransaction().detach(activeFragment!!).commit()
                activeFragment = fragment
                supportFragmentManager.beginTransaction()
//                        .detach(activeFragment!!)
                        .attach(activeFragment!!)
                        .commit()
                showEnterAnimation()

            })
                    ?.apply { disposables.add(this) }

    }

    override fun setFragment(fragmentTag: String) {
        supportFragmentManager.findFragmentByTag(fragmentTag)?.let {
            viewModel.setActiveFragment(it)
        }
    }


    private fun showEnterAnimation() {
        (activeFragment as? AnimatedFragment)?.showEnterAnimation()?.subscribe({}, {throwable ->
            Logi.logIt("No enter animation. Error: $throwable")
        })?.apply { disposables.add(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            println("Пользователь покинул активити")
        }
        //isFinishing() вернет false при перевороте экрана isFinishing() вернет false
        //isFinishing() вернет true если пользователь нажал back
        photosFragment = null
        interestFragment = null
        searchFragment = null
        favoritesFragment = null
        activeFragment = null
        disposables.dispose()
    }


    /*-----*/
    private fun loadButtonClickTest() {
        searchButton!!.setOnClickListener { v: View? ->
            val okHttpClient = OkHttpClient()
            val urlBuilder = HttpUrl.parse("https://www.flickr.com/services/rest/")!!.newBuilder()
            urlBuilder
                    .addQueryParameter("method", "flickr.photos.getRecent")
                    .addQueryParameter("api_key", "37ffb3155aa34e0e22081fc94dbabe2e")
                    .addQueryParameter("format", "json")
                    .addQueryParameter("nojsoncallback", "1")
            val url = urlBuilder.build().toString()
            println("URL: $url")
            val request = Request.Builder().url(url).build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val responseHeaders = response.headers()
                    for (i in 0 until responseHeaders.size()) {
                        println(responseHeaders.name(i) + ": " + responseHeaders.value(i))
                    }
                    println("ОТВЕТ: " + response.body()!!.string())
                }
            })
        }
    }
}