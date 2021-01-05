package ru.mihassu.photos.ui.paging;

import androidx.paging.DataSource;

import ru.mihassu.photos.domain.Photo;
import ru.mihassu.photos.interactor.SearchInteractor;


public class PhotosPositionalDataSourceFactory extends DataSource.Factory<Integer, Photo> {

    private SearchInteractor searchInteractor;
    private String query;

    public PhotosPositionalDataSourceFactory(SearchInteractor searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    @Override
    public DataSource<Integer, Photo> create() {
        return new PhotosPositionalDataSource(searchInteractor, query);
    }

    public void updateQuery(String query) {
        this.query = query;
    }
}
