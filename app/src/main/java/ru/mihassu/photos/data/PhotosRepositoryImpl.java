package ru.mihassu.photos.data;

import java.util.List;

import io.reactivex.Single;
import ru.mihassu.photos.domain.PhotoComment;
import ru.mihassu.photos.domain.PhotoPage;
import ru.mihassu.photos.domain.PhotoSize;
import ru.mihassu.photos.repository.PhotosRepository;

public class PhotosRepositoryImpl implements PhotosRepository {

    private PhotosDataSource photosDataSource;

    public PhotosRepositoryImpl(PhotosDataSource photosDataSource) {
        this.photosDataSource = photosDataSource;
    }

    @Override
    public Single<PhotoPage> getRecentPhotos(int pageNumber, int perPage) {
        return photosDataSource.getRecent(pageNumber, perPage);
    }

    @Override
    public Single<PhotoPage> searchPhotos(String query, int pageNumber, int perPage) {
        return photosDataSource.search(query, pageNumber, perPage);
    }

    @Override
    public Single<List<PhotoSize>> getPhotoSizes(String photoId) {
        return photosDataSource.getPhotoSizes(photoId);
    }

    @Override
    public Single<List<PhotoComment>> getPhotoComments(String photoId) {
        return photosDataSource.getPhotoComments(photoId);
    }

    @Override
    public Single<PhotoPage> getInterestingPhotos(String date, int pageNumber, int perPage) {
        return photosDataSource.getInterestingPhotos(date, pageNumber, perPage);
    }

    //    private final String GET_RECENT_METHOD = "flickr.photos.getRecent";
//    private final String SEARCH_METHOD = "flickr.photos.search";
//    private final String API_KEY = "37ffb3155aa34e0e22081fc94dbabe2e";
//    private final int PER_PAGE = 30;
//    private final int PAGE = 1;
//    private final String FORMAT = "json";
//    private final int NO_JSON_CALLBACK = 1;
//
//    private FlickrApi flickrApi;
//
//    public PhotosRepositoryImpl(FlickrApi flickrApi) {
//        this.flickrApi = flickrApi;
//    }
//
//    public Single<List<Photo>> getPhotosList() {
//
//        return flickrApi.getPhotos(GET_RECENT_METHOD, API_KEY, PER_PAGE, PAGE, FORMAT, NO_JSON_CALLBACK)
//                .map(photosRequest -> {
//                            List<Photo> photosList = photosRequest
//                                    .getPhotos()
//                                    .getPhotosList()
//                                    .stream()
//                                    .map(p -> {
//                                        String url = getUrl(p.getFarm(), p.getServer(), p.getId(), p.getSecret());
//                                        return new Photo(p.getId(), p.getTitle(), url);
//                                    })
//                                    .collect(Collectors.toList());
//                            return photosList;
//                }
//                )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public Single<List<Photo>> searchPhotos(String searchText) {
//
//        return flickrApi.searchPhotos(SEARCH_METHOD, API_KEY, searchText, FORMAT, NO_JSON_CALLBACK)
//                .map(photosRequest -> {
//                            List<Photo> photosList = photosRequest
//                                    .getPhotos()
//                                    .getPhotosList()
//                                    .stream()
//                                    .map(p -> {
//                                        String url = getUrl(p.getFarm(), p.getServer(), p.getId(), p.getSecret());
//                                        return new Photo(p.getId(), p.getTitle(), url);
//                                    })
//                                    .collect(Collectors.toList());
//                            return photosList;
//                        }
//                )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    private String getUrl(long farm, long server, long id, String secret) {
//        StringBuilder sb = new StringBuilder();
//        sb
//                .append("https://farm")
//                .append(farm)
//                .append(".staticflickr.com/")
//                .append(server)
//                .append("/")
//                .append(id)
//                .append("_")
//                .append(secret)
//                .append("_b.jpg");
//        return sb.toString();
//    }
//
//    public Single<List<Photo>> getPhotosListByPage(int page) {
//
//        int loadPage = page / PER_PAGE + 1;
//        Logi.logIt("loadPage = " + loadPage);
//        return flickrApi.getPhotos(GET_RECENT_METHOD, API_KEY, PER_PAGE, loadPage, FORMAT, NO_JSON_CALLBACK)
//                .map(photosRequest -> {
//                            List<Photo> photosList = photosRequest
//                                    .getPhotos()
//                                    .getPhotosList()
//                                    .stream()
//                                    .map(p -> {
//                                        String url = getUrl(p.getFarm(), p.getServer(), p.getId(), p.getSecret());
//                                        return new Photo(p.getId(), p.getTitle(), url);
//                                    })
//                                    .collect(Collectors.toList());
//                            return photosList;
//                        }
//                )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
