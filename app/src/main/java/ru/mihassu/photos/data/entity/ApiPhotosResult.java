package ru.mihassu.photos.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiPhotosResult {

    @SerializedName("photos")
    @Expose
    private ApiPhotos photos;

    public ApiPhotos getPhotos() {
        return photos;
    }
}
