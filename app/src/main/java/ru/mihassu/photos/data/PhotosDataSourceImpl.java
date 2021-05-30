package ru.mihassu.photos.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ru.mihassu.photos.common.Logi;
import ru.mihassu.photos.data.entity.Comment;
import ru.mihassu.photos.data.entity.Comments;
import ru.mihassu.photos.data.entity.SizeApi;
import ru.mihassu.photos.data.entity.SizesResponse;
import ru.mihassu.photos.data.entity.info.PhotoInfoResponse;
import ru.mihassu.photos.domain.PhotoComment;
import ru.mihassu.photos.domain.PhotoPage;
import ru.mihassu.photos.domain.PhotoSize;
import ru.mihassu.photos.network.FlickrApi;

public class PhotosDataSourceImpl implements PhotosDataSource {

    private final String GET_RECENT_METHOD = "flickr.photos.getRecent";
    private final String SEARCH_METHOD = "flickr.photos.search";
    private final String GET_SIZES_METHOD = "flickr.photos.getSizes";
    private final String GET_COMMENTS_METHOD = "flickr.photos.comments.getList";
    private final String GET_INTERESTING_METHOD = "flickr.interestingness.getList";
    private final String GET_PHOTO_INFO_METHOD = "flickr.photos.getInfo";
    private final String API_KEY = "37ffb3155aa34e0e22081fc94dbabe2e";
    private final String JSON_FORMAT = "json";
    private final int NO_JSON_CALLBACK = 1;
    private FlickrApi flickrApi;

    public PhotosDataSourceImpl(FlickrApi flickrApi) {
        this.flickrApi = flickrApi;
    }

    @Override
    public Single<PhotoPage> getRecent(int pageNumber, int perPage) {
        return flickrApi
                .getPhotos(GET_RECENT_METHOD, API_KEY, perPage, pageNumber, JSON_FORMAT, NO_JSON_CALLBACK)
                .map(PhotosMapper::map)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<PhotoPage> search(String query, int pageNumber, int perPage) {
        return flickrApi
                .searchPhotos(SEARCH_METHOD, API_KEY, query, JSON_FORMAT, perPage, pageNumber, NO_JSON_CALLBACK)
                .map(PhotosMapper::map);
    }

    @Override
    public Single<List<PhotoSize>> getPhotoSizes(String photoId) {
        return flickrApi
                .getSizes(GET_SIZES_METHOD, API_KEY, photoId, JSON_FORMAT, NO_JSON_CALLBACK)
                .doOnError(throwable -> Logi.logIt("doOnError:" + throwable.getMessage()))
                .map(new Function<SizesResponse, List<PhotoSize>>() {
                    @Override
                    public List<PhotoSize> apply(@NonNull SizesResponse sizesResponse) throws Exception {
                        List<PhotoSize> sizesList = new ArrayList<>();
                        if (sizesResponse.getSizes() != null) {
                            for (SizeApi photoSize : sizesResponse.getSizes().getSizesList()) {
                                sizesList.add(new PhotoSize(photoSize.getLabel(), photoSize.getWidth(), photoSize.getHeight(), photoSize.getSource(), photoSize.getUrl(), photoSize.getMedia()));
                            }
                        }
                        return sizesList;
                    }
                });
//                .map(sizesResultApi -> {
//                    List<PhotoSize> sizesList = new ArrayList<>();
//                    for (SizeApi photoSize: sizesResultApi.getSizes().getSizesList()) {
//                        sizesList.add(new PhotoSize(photoSize.getLabel(), photoSize.getWidth(), photoSize.getHeight(), photoSize.getSource(), photoSize.getUrl(), photoSize.getMedia()));
//                    }
//                    return sizesList;
//                });
    }

    @Override
    public Single<List<PhotoComment>> getPhotoComments(String photoId) {

        return flickrApi
                .getComments(GET_COMMENTS_METHOD, API_KEY, photoId, JSON_FORMAT, NO_JSON_CALLBACK)
                .map(commentsResponse -> {
                    List<PhotoComment> commentsList = new ArrayList<>();
                    if ((commentsResponse.getComments() != null) && (commentsResponse.getComments().getComment() != null)) {
                        for (Comment comments: commentsResponse.getComments().getComment()) {
                            commentsList.add(new PhotoComment(comments.getId(), comments.getAuthorname(), comments.getContent()));
                        }
                    }
                    return commentsList;
                });


    }

    @Override
    public Single<PhotoPage> getInterestingPhotos(String date, int pageNumber, int perPage) {
        return flickrApi
                .getInterestingPhotos(GET_INTERESTING_METHOD, API_KEY, date, perPage, pageNumber, JSON_FORMAT, NO_JSON_CALLBACK)
                .map(PhotosMapper::mapInterest)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<PhotoInfoResponse> getPhotoInfo(String photoId) {
        return flickrApi.getPhotoInfo(GET_PHOTO_INFO_METHOD, API_KEY, photoId, JSON_FORMAT, NO_JSON_CALLBACK);
    }
}
