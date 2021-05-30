package ru.mihassu.photos.data.entity.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class CommentsInfo {

    @SerializedName("_content")
    @Expose
    private Integer content;

    public Integer getContent() {
        return content;
    }

    public void setContent(Integer content) {
        this.content = content;
    }
}
