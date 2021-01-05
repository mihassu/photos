package ru.mihassu.photos.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class ApiPhotos {
    @SerializedName("page")
    @Expose
    val page: Long = 0

    @SerializedName("pages")
    @Expose
    val pages: Long = 0

    @SerializedName("perpage")
    @Expose
    val perpage: Long = 0

    @SerializedName("total")
    @Expose
    val total = 0

    @SerializedName("photo")
    @Expose
    val photosList: ArrayList<ApiPhoto>? = null
}