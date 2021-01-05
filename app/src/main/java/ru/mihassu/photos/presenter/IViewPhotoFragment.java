package ru.mihassu.photos.presenter;

import com.arellomobile.mvp.MvpView;

public interface IViewPhotoFragment extends MvpView {
    void showPhoto(String url);
}
