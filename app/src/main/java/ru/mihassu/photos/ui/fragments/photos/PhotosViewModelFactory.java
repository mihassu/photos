package ru.mihassu.photos.ui.fragments.photos;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mihassu.photos.interactor.SearchInteractor;
import ru.mihassu.photos.repository.PhotosRepository;
import ru.mihassu.photos.ui.db.DataBaseInteractor;
import ru.mihassu.photos.ui.paging.PhotosFragmentViewModelPaging;
import ru.mihassu.photos.ui.paging.PhotosPositionalDataSourceFactory;

public class PhotosViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private PhotosPositionalDataSourceFactory photosPositionalDataSourceFactory;
    private SearchInteractor searchInteractor;
    private DataBaseInteractor dataBaseInteractor;
    private PhotosRepository photosRepository;

    public PhotosViewModelFactory(PhotosPositionalDataSourceFactory photosPositionalDataSourceFactory) {
        this.photosPositionalDataSourceFactory = photosPositionalDataSourceFactory;
    }

    public PhotosViewModelFactory(PhotosRepository photosRepository, DataBaseInteractor dataBaseInteractor) {
        this.photosRepository = photosRepository;
        this.dataBaseInteractor = dataBaseInteractor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == PhotosFragmentViewModelPaging.class) {
            return (T) new PhotosFragmentViewModelPaging(photosPositionalDataSourceFactory);
        } else if (modelClass == PhotosViewModel.class) {
            return (T) new PhotosViewModel(photosRepository, dataBaseInteractor);
        }
        return null;
    }
}
