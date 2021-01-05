package ru.mihassu.photos.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {

    private Context context;

    public AppContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @AppScope
    Context getAppContext(){
        return context.getApplicationContext();
    }
}
