package ru.mihassu.photos.ui.fragments.favorite;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mihassu.photos.ui.db.DataBaseInteractor;

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
