
package ru.mihassu.photos.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterestResponse {

    @SerializedName("photos")
    @Expose
    private ApiPhotos photos;

    @SerializedName("extra")
    @Expose
    private InterestExtra interestExtra;

    @SerializedName("stat")
    @Expose
    private String stat;

    public ApiPhotos getPhotos() {
        return photos;
    }

    public void setPhotos(ApiPhotos photos) {
        this.photos = photos;
    }

    public InterestExtra getInterestExtra() {
        return interestExtra;
    }

    public void setInterestExtra(InterestExtra interestExtra) {
        this.interestExtra = interestExtra;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
