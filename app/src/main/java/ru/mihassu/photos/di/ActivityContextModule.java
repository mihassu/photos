package ru.mihassu.photos.di;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityContextModule {

    private AppCompatActivity activity;

    public ActivityContextModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    AppCompatActivity getActivityContext(){
        return activity;
    }
}
