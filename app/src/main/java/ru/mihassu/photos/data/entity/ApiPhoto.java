package ru.mihassu.photos.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiPhoto {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("owner")
    @Expose
    private String owner;

    @SerializedName("secret")
    @Expose
    private String secret;

    @SerializedName("server")
    @Expose
    private long server;

    @SerializedName("farm")
    @Expose
    private long farm;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("ispublic")
    @Expose
    private Integer ispublic;

    @SerializedName("isfriend")
    @Expose
    private Integer isfriend;

    @SerializedName("isfamily")
    @Expose
    private Integer isfamily;


    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public long getServer() {
        return server;
    }

    public long getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }
}
