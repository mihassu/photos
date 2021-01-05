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
}
