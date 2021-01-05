package ru.mihassu.photos.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mihassu.photos.network.FlickrApi;

@Module
public class NetworkModule {

    private static final String BASE_URL = "https://www.flickr.com/";


    @Provides
    @AppScope
    FlickrApi provideFlickrApi(Retrofit retrofit) {
        return retrofit.create(FlickrApi.class);
    }

    @Provides
    @AppScope
    Retrofit provideRetrofit(Gson gson){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @AppScope
    GsonBuilder provideGsonBuilder(){
        return new GsonBuilder();
    }

    @Provides
    @AppScope
    Gson providesGson(GsonBuilder gsonBuilder){
        return gsonBuilder.create();
    }
}
