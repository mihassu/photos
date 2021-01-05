package ru.mihassu.photos.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class PhotoPage {
    private List<Photo> photosList;
    private int total;
    private int page;
    private int pages;

    public PhotoPage(List<Photo> photosList, int total, long page, long pages) {
        this.photosList = photosList;
        this.total = total;
        this.page = (int) page;
        this.pages = (int) pages;
    }

    public List<Photo> getPhotosList() {
        return photosList;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PhotoPage another = (PhotoPage) obj;
        return this.total == another.total && Objects.equals(this.photosList, another.photosList);
    }

    @NonNull
    @Override
    public String toString() {
        return "PhotoPage {photosList: " + photosList + ", total: " + total + "}";
    }
}
