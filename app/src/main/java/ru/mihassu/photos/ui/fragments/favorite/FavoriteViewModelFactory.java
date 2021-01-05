package ru.mihassu.photos.ui.fragments.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mihassu.photos.interactor.SearchInteractor;
import ru.mihassu.photos.ui.db.DataBaseInteractor;
import ru.mihassu.photos.ui.fragments.PhotosFragmentViewModelPaging;
import ru.mihassu.photos.ui.fragments.photos.PhotosViewModel;
import ru.mihassu.photos.ui.paging.PhotosPositionalDataSourceFactory;

public class FavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private DataBaseInteractor dataBaseInteractor;


    public FavoriteViewModelFactory(DataBaseInteractor dataBaseInteractor) {

        this.dataBaseInteractor = dataBaseInteractor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == FavoriteViewModel.class) {
            return (T) new FavoriteViewModel(dataBaseInteractor);
        }
        return null;
    }
}
