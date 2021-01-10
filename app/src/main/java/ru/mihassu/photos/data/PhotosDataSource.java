package ru.mihassu.photos.data;

import java.util.List;

import io.reactivex.Single;
import ru.mihassu.photos.domain.PhotoComment;
import ru.mihassu.photos.domain.PhotoPage;
import ru.mihassu.photos.domain.PhotoSize;

public interface PhotosDataSource {
    Single<PhotoPage> getRecent(int pageNumber, int perPage);
    Single<PhotoPage> search(String query, int pageNumber, int perPage);
    Single<List<PhotoSize>> getPhotoSizes(String photoId);
    Single<List<PhotoComment>> getPhotoComments(String photoId);
}
