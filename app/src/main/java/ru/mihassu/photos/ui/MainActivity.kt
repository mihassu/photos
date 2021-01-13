package ru.mihassu.photos.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import ru.mihassu.photos.R
import ru.mihassu.photos.di.ActivityComponent
import ru.mihassu.photos.di.ActivityContextModule
import ru.mihassu.photos.di.DaggerActivityComponent
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val searchButton: Button? = null
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    companion object {
        @JvmStatic
        lateinit var activityComponent: ActivityComponent
    }

    //    //Если конструктор презентера с параметрами
    //    @ProvidePresenter
    //    MainPresenter provideMainPresenter() {
    //        return new MainPresenter(App.getAppComponent().getPhotosRepository());
    //    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent = DaggerActivityComponent.builder()
                .activityContextModule(ActivityContextModule(this))
                .build()
//        navController = Navigation.findNavController(this, R.id.nav_host_bottom)
//        initViews()
    }


    private fun initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            println("Пользователь покинул активити")
        }
        //isFinishing() вернет false при перевороте экрана isFinishing() вернет false
        //isFinishing() вернет true если пользователь нажал back
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