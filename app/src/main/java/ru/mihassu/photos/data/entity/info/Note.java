
package ru.mihassu.photos.data.entity.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("photo_id")
    @Expose
    private String photoId;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("authorname")
    @Expose
    private String authorname;
    @SerializedName("authorrealname")
    @Expose
    private String authorrealname;
    @SerializedName("authorispro")
    @Expose
    private Integer authorispro;
    @SerializedName("authorisdeleted")
    @Expose
    private Integer authorisdeleted;
    @SerializedName("x")
    @Expose
    private String x;
    @SerializedName("y")
    @Expose
    private Integer y;
    @SerializedName("w")
    @Expose
    private Integer w;
    @SerializedName("h")
    @Expose
    private Integer h;
    @SerializedName("_content")
    @Expose
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getAuthorrealname() {
        return authorrealname;
    }

    public void setAuthorrealname(String authorrealname) {
        this.authorrealname = authorrealname;
    }

    public Integer getAuthorispro() {
        return authorispro;
    }

    public void setAuthorispro(Integer authorispro) {
        this.authorispro = authorispro;
    }

    public Integer getAuthorisdeleted() {
        return authorisdeleted;
    }

    public void setAuthorisdeleted(Integer authorisdeleted) {
        this.authorisdeleted = authorisdeleted;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
