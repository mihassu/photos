package ru.mihassu.photos.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mihassu.photos.data.entity.ApiPhotosResponse;
import ru.mihassu.photos.data.entity.CommentsResponse;
import ru.mihassu.photos.data.entity.InterestResponse;
import ru.mihassu.photos.data.entity.SizesResponse;
import ru.mihassu.photos.data.entity.info.PhotoInfoResponse;

public interface FlickrApi {

    @GET("services/rest/")
    Single<ApiPhotosResponse> getPhotos(@Query("method")String method,
                                        @Query("api_key")String apiKey,
                                        @Query("per_page") int per,
                                        @Query("page") int page,
                                        @Query("format") String format,
                                        @Query("nojsoncallback") int n);

    @GET("services/rest/")
    Single<ApiPhotosResponse> searchPhotos(@Query("method")String method,
                                           @Query("api_key")String apiKey,
                                           @Query("text") String text,
                                           @Query("format") String format,
                                           @Query("per_page") int per,
                                           @Query("page") int page,
                                           @Query("nojsoncallback") int n);

    @GET("services/rest/")
    Single<SizesResponse> getSizes(@Query("method")String method,
                                   @Query("api_key")String apiKey,
                                   @Query("photo_id") String photoId,
                                   @Query("format") String format,
                                   @Query("nojsoncallback") int n);

    @GET("services/rest/")
    Single<CommentsResponse> getComments(@Query("method")String method,
                                         @Query("api_key")String apiKey,
                                         @Query("photo_id") String photoId,
                                         @Query("format") String format,
                                         @Query("nojsoncallback") int n);

    @GET("services/rest/")
    Single<InterestResponse> getInterestingPhotos(@Query("method")String method,
                                                  @Query("api_key")String apiKey,
                                                  @Query("date")String date,
                                                  @Query("per_page") int per,
                                                  @Query("page") int page,
                                                  @Query("format") String format,
                                                  @Query("nojsoncallback") int n);

    @GET("services/rest/")
    Single<PhotoInfoResponse> getPhotoInfo(@Query("method")String method,
                                           @Query("api_key")String apiKey,
                                           @Query("photo_id") String photoId,
                                           @Query("format") String format,
                                           @Query("nojsoncallback") int n);

}
