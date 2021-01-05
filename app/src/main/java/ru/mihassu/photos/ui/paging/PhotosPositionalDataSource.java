package ru.mihassu.photos.ui.paging;

import androidx.annotation.NonNull;
import androidx.paging.PositionalDataSource;

import io.reactivex.disposables.CompositeDisposable;
import ru.mihassu.photos.common.Logi;
import ru.mihassu.photos.domain.Photo;
import ru.mihassu.photos.interactor.SearchInteractor;

public class PhotosPositionalDataSource extends PositionalDataSource<Photo> {

    private SearchInteractor searcInteractor;
    private String query;
    private int pageSize;
    private CompositeDisposable disposables = new CompositeDisposable();


    public PhotosPositionalDataSource(SearchInteractor searcInteractor, String query) {
        this.searcInteractor = searcInteractor;
        this.query = query;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Photo> callback) {
//        int startPage = params.requestedStartPosition / params.pageSize; //(searcInteractor.getPhotos(query, startPage, pageSize)
//        final int startPosition = startPage * params.pageSize;
//        pageSize = params.pageSize;

        Logi.logIt("loadInitial():" +
                  "\nrequestedStartPosition: " + params.requestedStartPosition +
//                "\npageSize: " + params.pageSize +
                "\nrequestedLoadSize: " + params.requestedLoadSize
//                "\nstartPage: " + startPage +
//                "\nstartPosition: " + startPosition
        );

        disposables.add(searcInteractor.getPhotos(query, params.requestedStartPosition, params.requestedLoadSize)
                .subscribe(photoPage -> {
                    if (params.placeholdersEnabled) {
                        callback.onResult(photoPage.getPhotosList(), photoPage.getPage(), photoPage.getTotal());
                    } else {
                        callback.onResult(photoPage.getPhotosList(), photoPage.getPage());
                    }
                }, throwable -> System.out.println("Ошибка при получении данных в loadInitial(): " + throwable.getMessage())));
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Photo> callback) {
//        int page = params.startPosition / pageSize; //searcInteractor.getPhotos(query, page, pageSize)

        Logi.logIt("loadRange():" +
                "\nstartPosition: " + params.startPosition +
                "\nloadSize: " + params.loadSize
//                "\npage: " + page
        );

        disposables.add(searcInteractor.getPhotos(query, params.startPosition, params.loadSize)
                .subscribe(photoPage -> {
                    callback.onResult(photoPage.getPhotosList());
                }, throwable -> System.out.println("Ошибка при получении данных: " + throwable.getMessage())));
    }

    public void onPresenterDestroy() {
        disposables.dispose();
    }
}
