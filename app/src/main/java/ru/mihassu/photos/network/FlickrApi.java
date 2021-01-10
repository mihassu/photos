package ru.mihassu.photos.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.mihassu.photos.data.entity.ApiPhotosResult;
import ru.mihassu.photos.data.entity.CommentsResponse;
import ru.mihassu.photos.data.entity.SizesResultApi;

public interface FlickrApi {

    @GET("services/rest/")
    Single<ApiPhotosResult> getPhotos(@Query("method")String method,
                                      @Query("api_key")String apiKey,
                                      @Query("per_page") int per,
                                      @Query("page") int page,
                                      @Query("format") String format,
                                      @Query("nojsoncallback") int n);

    @GET("services/rest/")
    Single<ApiPhotosResult> searchPhotos(@Query("method")String method,
                                         @Query("api_key")String apiKey,
                                         @Query("text") String text,
                                         @Query("format") String format,
                                         @Query("per_page") int per,
                                         @Query("page") int page,
                                         @Query("nojsoncallback") int n);

    @GET("services/rest/")
    Single<SizesResultApi> getSizes(@Query("method")String method,
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
}
