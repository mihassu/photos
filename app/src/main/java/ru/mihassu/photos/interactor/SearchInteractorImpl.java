package ru.mihassu.photos.interactor;

import io.reactivex.Observable;
import ru.mihassu.photos.common.Logi;
import ru.mihassu.photos.domain.PhotoPage;
import ru.mihassu.photos.repository.PhotosRepository;

public class SearchInteractorImpl implements SearchInteractor {

    private PhotosRepository photosRepository;
//    private int pageNumber;
//    private int totalPages;

    public SearchInteractorImpl(PhotosRepository photosRepository) {
        this.photosRepository = photosRepository;
//        pageNumber = 0;
//        totalPages = 10;
    }

    @Override
    public Observable<PhotoPage> getPhotos(String query, int pageNumber, int perPage) {
        if (query.isEmpty()) {
            Logi.logIt("SearchInteractor - getRecentPhotos()\n" + "pageNumber = " + pageNumber + "\n perPage = " + perPage);
            return photosRepository.getRecentPhotos(pageNumber, perPage).toObservable();
        } else {
//            Logi.logIt("SearchInteractor - query = " + query + " searchPhotos()\n" + "pageNumber = " + pageNumber + " perPage = " + perPage);
            return photosRepository.searchPhotos(query, pageNumber, perPage).toObservable();
        }
    }
}
