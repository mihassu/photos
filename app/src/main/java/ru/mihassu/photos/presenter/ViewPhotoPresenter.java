package ru.mihassu.photos.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.mihassu.photos.App;
import ru.mihassu.photos.data.PhotosRepositoryImpl;
import ru.mihassu.photos.repository.PhotosRepository;

@InjectViewState
public class ViewPhotoPresenter extends MvpPresenter<IViewPhotoFragment> {

    private PhotosRepository photosRepository;

    public ViewPhotoPresenter() {
        this.photosRepository = App.getAppComponent().getPhotosRepository();
    }

    public void loadPhotoByUrl(String url) {

    }
}
