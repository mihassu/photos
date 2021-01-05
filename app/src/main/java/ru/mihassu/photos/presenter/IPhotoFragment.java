package ru.mihassu.photos.presenter;

import androidx.paging.PagedList;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.mihassu.photos.domain.Photo;

public interface IPhotoFragment extends MvpView {

    void showPhotos(List<Photo> photos);
    void showPhotosPaging(PagedList<Photo> photosPagedList);
    void showProgress();
    void hideProgress();
    void showError(String text);
}
