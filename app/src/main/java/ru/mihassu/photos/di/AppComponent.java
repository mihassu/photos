package ru.mihassu.photos.di;

import com.squareup.picasso.Picasso;

import dagger.Component;
import ru.mihassu.photos.presenter.PhotosPresenter;
import ru.mihassu.photos.repository.PhotosRepository;
import ru.mihassu.photos.ui.MainActivity;
import ru.mihassu.photos.ui.fragments.favorite.FavoriteFragment;
import ru.mihassu.photos.ui.fragments.interest.InterestFragment;
import ru.mihassu.photos.ui.fragments.photo.SinglePhotoFragment;
import ru.mihassu.photos.ui.fragments.photos.PhotosFragment;
import ru.mihassu.photos.ui.fragments.search.SearchFragment;

@Component (modules = {RepositoryModule.class, PicassoModule.class, RoomModule.class})
@AppScope
public interface AppComponent {

    Picasso getPicasso();
    PhotosRepository getPhotosRepository();

    void inject(MainActivity mainActivity);
    void inject(PhotosPresenter mainPresenter);
    void inject(PhotosFragment photosFragment);
    void inject(FavoriteFragment favoriteFragment);
    void inject(SinglePhotoFragment singlePhotoFragment);
    void inject(SearchFragment searchFragment);
    void inject(InterestFragment searchFragment);

}
