package ru.mihassu.photos.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comments (

    @SerializedName("photo_id")
    @Expose
    var photoId: String,

    @SerializedName("comment")
    @Expose
    var comment: List<Comment>?
    )