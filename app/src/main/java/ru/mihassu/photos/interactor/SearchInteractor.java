package ru.mihassu.photos.interactor;

import io.reactivex.Observable;
import ru.mihassu.photos.domain.PhotoPage;

public interface SearchInteractor {

    Observable<PhotoPage> getPhotos(String query, int pageNumber, int perPage);
}
