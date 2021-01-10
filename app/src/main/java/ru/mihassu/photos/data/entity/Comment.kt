package ru.mihassu.photos.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comment (

    @SerializedName("id")
    @Expose
    var  id: String,

    @SerializedName("author")
    @Expose
    var author: String,

    @SerializedName("author_is_deleted")
    @Expose
    var authorIsDeleted: Int,

    @SerializedName("authorname")
    @Expose
    var authorname: String,

    @SerializedName("iconserver")
    @Expose
    var iconserver: Int,

    @SerializedName("iconfarm")
    @Expose
    var iconfarm: Int,

    @SerializedName("datecreate")
    @Expose
    var datecreate: String,

    @SerializedName("permalink")
    @Expose
    var permalink: String,

    @SerializedName("path_alias")
    @Expose
    var pathAlias: String,

    @SerializedName("realname")
    @Expose
    var realname: String,

    @SerializedName("_content")
    @Expose
    var content: String
)