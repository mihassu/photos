package ru.mihassu.photos.repository;

import java.util.List;

import io.reactivex.Single;
import ru.mihassu.photos.domain.Photo;
import ru.mihassu.photos.domain.PhotoComment;
import ru.mihassu.photos.domain.PhotoPage;
import ru.mihassu.photos.domain.PhotoSize;

public interface PhotosRepository {

    Single<PhotoPage> getRecentPhotos(int pageNumber, int perPage);
    Single<PhotoPage> searchPhotos(String query, int pageNumber, int perPage);
//    Single<List<PhotoSize>> getPhotoSizes(String photoId);
//    Single<List<PhotoComment>> getPhotoComments(String photoId);
    Single<PhotoPage> getInterestingPhotos(String date, int pageNumber, int perPage);
    Single<Photo> getPhotoById(Long photoId);
    Single<Boolean> toggleFavoritePhoto(Photo photo);
    Single<Boolean> checkFavoritePhoto(Photo photo);
}
