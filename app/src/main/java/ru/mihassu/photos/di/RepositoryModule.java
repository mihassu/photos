package ru.mihassu.photos.di;

import dagger.Module;
import dagger.Provides;
import ru.mihassu.photos.data.PhotosDataSource;
import ru.mihassu.photos.data.PhotosDataSourceImpl;
import ru.mihassu.photos.interactor.SearchInteractor;
import ru.mihassu.photos.interactor.SearchInteractorImpl;
import ru.mihassu.photos.network.FlickrApi;
import ru.mihassu.photos.data.PhotosRepositoryImpl;
import ru.mihassu.photos.repository.PhotosRepository;
import ru.mihassu.photos.ui.db.DataBaseInteractor;
import ru.mihassu.photos.ui.paging.PhotosPositionalDataSourceFactory;

@Module(includes = {NetworkModule.class, RoomModule.class})
public class RepositoryModule {

    @Provides
    @AppScope
    PhotosDataSource providePhotosDataSource(FlickrApi flickrApi) {
        return new PhotosDataSourceImpl(flickrApi);
    }

    @Provides
    @AppScope
    PhotosRepository providePhotosRepository(PhotosDataSource photosDataSource, DataBaseInteractor dataBaseInteractor) {
        return new PhotosRepositoryImpl(photosDataSource, dataBaseInteractor);
    }


    @Provides
    @AppScope
    SearchInteractor provideSearchInteractor(PhotosRepository photosRepository) {
        return new SearchInteractorImpl(photosRepository);
    }

    @Provides
    @AppScope
    PhotosPositionalDataSourceFactory providePhotosPositionalDataSourceFactory(SearchInteractor searchInteractor) {
        return new PhotosPositionalDataSourceFactory(searchInteractor);
    }
}
