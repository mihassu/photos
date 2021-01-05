package ru.mihassu.photos.presenter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.mihassu.photos.App;
import ru.mihassu.photos.common.Logi;
import ru.mihassu.photos.domain.Photo;
import ru.mihassu.photos.ui.paging.PhotosPositionalDataSourceFactory;


@InjectViewState
public class PhotosPresenter extends MvpPresenter<IPhotoFragment> {

    private List<Photo> photos;

    @Inject
    PhotosPositionalDataSourceFactory photosPositionalDataSourceFactory;
    private CompositeDisposable disposables = new CompositeDisposable();
    private PagedList.Config config;
    private String currentQuery = "";
    private boolean isFirstQuery = true;
    private PhotosLiveData photosLiveData;
    private PagedList.Builder<Integer, Photo> pagedListBuilder;
    private Executor executor;
    private LiveData<PagedList<Photo>> pagedListLiveData;

    public PhotosPresenter() {
        App.getAppComponent().inject(this);

        config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(30)
                .setInitialLoadSizeHint(60)
                .setPrefetchDistance(10)
                .build();

        photosLiveData = new PhotosLiveData();
        onSearchQueryUpdated("");
//        executor = Executors.newSingleThreadExecutor();
//        pagedListBuilder =
//                new PagedList.Builder<>(photosPositionalDataSourceFactory.create(), config)
//                        .setFetchExecutor(executor)
//                        .setNotifyExecutor(new MainThreadExecutor());
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Logi.logIt("PhotosPresenter - onFirstViewAttach()");

    }

    public PhotosLiveData getPhotosLiveData() {
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
                .build();
        photosLiveData.update(pagedListLiveData);
    }




//    public void loadRecentPhotos() {
//        getViewState().showProgress();
//        disposables.add(photosRepository.getPhotosList()
//                .subscribe(photoConverts -> {
//                            photos = photoConverts;
//                            getViewState().showPhotos(photos);
//                            Thread.sleep(500);
//                            getViewState().hideProgress();
//                        }
//                        , throwable -> {
//                            System.out.println("Ошибка при получении данных: " + throwable.getMessage());
//                            getViewState().showError("Ошибка при получении данных");
//                            getViewState().hideProgress();
//                        }
//                )
//        );
//    }
//
//    public void searchPhotos(String searchText) {
//        getViewState().showProgress();
//        if (!searchText.equals("")) {
//            disposables.add(photosRepository.searchPhotos(searchText)
//                    .subscribe(photoConverts -> {
//                                photos = photoConverts;
//                                getViewState().showPhotos(photos);
//                                Thread.sleep(500);
//                                getViewState().hideProgress();
//                            }
//                            , throwable -> System.out.println("Ошибка при получении данных: " + throwable.getMessage())
//                    )
//            );
//        }
//    }
//
//    public void onFragmentResume(Activity activity) {
//        executor.execute(() -> {
//            final PagedList<Photo> pagedList = pagedListBuilder.build();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            activity.runOnUiThread(() -> getViewState().showPhotosPaging(pagedList));
//        });
//    }


    @Override
    public void onDestroy() {
        disposables.dispose();
        Logi.logIt("PhotosPresenter - onDestroy()");
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

}
