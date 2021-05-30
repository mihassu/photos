package ru.mihassu.photos.data.entity.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoInfo {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("server")
    @Expose
    private Integer server;
    @SerializedName("farm")
    @Expose
    private Integer farm;
    @SerializedName("dateuploaded")
    @Expose
    private String dateuploaded;
    @SerializedName("isfavorite")
    @Expose
    private Integer isfavorite;
    @SerializedName("license")
    @Expose
    private Integer license;
    @SerializedName("safety_level")
    @Expose
    private Integer safetyLevel;
    @SerializedName("rotation")
    @Expose
    private Integer rotation;
    @SerializedName("owner")
    @Expose
    private OwnerInfo owner;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("description")
    @Expose
    private Description description;
    @SerializedName("visibility")
    @Expose
    private Visibility visibility;
    @SerializedName("dates")
    @Expose
    private Dates dates;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("editability")
    @Expose
    private Editability editability;
    @SerializedName("publiceditability")
    @Expose
    private Publiceditability publiceditability;
    @SerializedName("usage")
    @Expose
    private Usage usage;
    @SerializedName("comments")
    @Expose
    private CommentsInfo comments;
    @SerializedName("notes")
    @Expose
    private Notes notes;
    @SerializedName("people")
    @Expose
    private People people;
    @SerializedName("tags")
    @Expose
    private Tags tags;
    @SerializedName("urls")
    @Expose
    private Urls urls;
    @SerializedName("media")
    @Expose
    private String media;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getServer() {
        return server;
    }

    public void setServer(Integer server) {
        this.server = server;
    }

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public String getDateuploaded() {
        return dateuploaded;
    }

    public void setDateuploaded(String dateuploaded) {
        this.dateuploaded = dateuploaded;
    }

    public Integer getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(Integer isfavorite) {
        this.isfavorite = isfavorite;
    }

    public Integer getLicense() {
        return license;
    }

    public void setLicense(Integer license) {
        this.license = license;
    }

    public Integer getSafetyLevel() {
        return safetyLevel;
    }

    public void setSafetyLevel(Integer safetyLevel) {
        this.safetyLevel = safetyLevel;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public OwnerInfo getOwner() {
        return owner;
    }

    public void setOwner(OwnerInfo owner) {
        this.owner = owner;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public Editability getEditability() {
        return editability;
    }

    public void setEditability(Editability editability) {
        this.editability = editability;
    }

    public Publiceditability getPubliceditability() {
        return publiceditability;
    }

    public void setPubliceditability(Publiceditability publiceditability) {
        this.publiceditability = publiceditability;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public CommentsInfo getComments() {
        return comments;
    }

    public void setComments(CommentsInfo comments) {
        this.comments = comments;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
