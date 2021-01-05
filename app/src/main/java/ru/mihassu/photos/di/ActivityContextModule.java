package ru.mihassu.photos.di;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityContextModule {

    private Activity activity;

    public ActivityContextModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity getActivityContext(){
        return activity;
    }
}
