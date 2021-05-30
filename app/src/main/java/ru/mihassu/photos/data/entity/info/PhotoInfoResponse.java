
package ru.mihassu.photos.data.entity.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoInfoResponse {

    @SerializedName("photo")
    @Expose
    private PhotoInfo photo;

    @SerializedName("stat")
    @Expose
    private String stat;

    public PhotoInfo getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoInfo photo) {
        this.photo = photo;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}
