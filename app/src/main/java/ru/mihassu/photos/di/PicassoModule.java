package ru.mihassu.photos.di;

import android.app.Activity;
import android.content.Context;

import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppContextModule.class)
public class PicassoModule {

    @Provides
    @AppScope
    Picasso providePicasso(Context context) {
        return new Picasso.Builder(context).build();
    }
}
