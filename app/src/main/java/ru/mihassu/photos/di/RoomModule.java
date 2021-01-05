package ru.mihassu.photos.di;

import android.content.Context;

import androidx.room.Room;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.photos.db.AppDataBase;
import ru.mihassu.photos.db.DataBaseInteractorImpl;
import ru.mihassu.photos.ui.db.DataBaseInteractor;

@Module(includes = AppContextModule.class)
public class RoomModule {

    @Provides
    @AppScope
    AppDataBase provideDataBase(Context context) {
        return AppDataBase.Companion.getInstance(context);
    }

    @Provides
    @AppScope
    DataBaseInteractor provideDataBaseInteractor(AppDataBase appDataBase) {
        return new DataBaseInteractorImpl(appDataBase);
    }
}
