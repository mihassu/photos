package ru.mihassu.photos.ui.paging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import io.reactivex.disposables.CompositeDisposable;
import ru.mihassu.photos.domain.Photo;

public class PhotosFragmentViewModelPaging extends ViewModel {

    private PhotosPositionalDataSourceFactory photosPositionalDataSourceFactory;
    private CompositeDisposable disposables = new CompositeDisposable();
    private PagedList.Config config;
    private String currentQuery = "";
    private boolean isFirstQuery = true;
    private PhotosFragmentViewModelPaging.PhotosLiveData photosLiveData;
    private LiveData<PagedList<Photo>> pagedListLiveData;

    public PhotosFragmentViewModelPaging(PhotosPositionalDataSourceFactory photosPositionalDataSourceFactory) {
        this.photosPositionalDataSourceFactory = photosPositionalDataSourceFactory;

        config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(10)
//                .setInitialLoadSizeHint(10)
//                .setPrefetchDistance(10)
                .build();

        photosLiveData = new PhotosFragmentViewModelPaging.PhotosLiveData();
        onSearchQueryUpdated("");
    }

    public PhotosFragmentViewModelPaging.PhotosLiveData getPhotosLiveData() {
        return photosLiveData;
    }

    public void onSearchQueryUpdated(String query) {
        if (!isFirstQuery && query.equals(currentQuery)) {
            return;
        }

        isFirstQuery = false;
        currentQuery = query;

        photosPositionalDataSourceFactory.updateQuery(query);

        pagedListLiveData = new LivePagedListBuilder<>(photosPositionalDataSourceFactory, config)
                .setInitialLoadKey(1)
                .build();
        photosLiveData.update(pagedListLiveData);
    }

    //MediatorLiveData - можно соединять несколько LiveData в один источник
    static class PhotosLiveData extends MediatorLiveData<PagedList<Photo>> {

        private LiveData<PagedList<Photo>> prevSource = null;

        void update(LiveData<PagedList<Photo>> newList) {
            if (prevSource != null) {
                removeSource(prevSource);
            }

            this.prevSource = newList;

            addSource(newList, new Observer<PagedList<Photo>>() {
                @Override
                public void onChanged(PagedList<Photo> photos) {
                    setValue(photos);
                }
            });
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}
