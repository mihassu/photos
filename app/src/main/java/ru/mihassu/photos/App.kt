package ru.mihassu.photos

import android.app.Application
import ru.mihassu.photos.di.*
import ru.mihassu.photos.di.DaggerAppComponent

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appContextModule(AppContextModule(applicationContext))
                .networkModule(NetworkModule())
                .picassoModule(PicassoModule())
                .repositoryModule(RepositoryModule())
                .build()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
            private set
    }
}