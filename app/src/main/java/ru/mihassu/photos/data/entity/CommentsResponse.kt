package ru.mihassu.photos.data.entity

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import ru.mihassu.photos.data.entity.Comments

data class CommentsResponse (

    @SerializedName("comments")
    @Expose
    var comments: Comments? = null,

    @SerializedName("stat")
    @Expose
    var stat: String
)