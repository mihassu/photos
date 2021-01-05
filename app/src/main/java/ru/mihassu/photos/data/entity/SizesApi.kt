package ru.mihassu.photos.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SizesApi {
        @SerializedName("canblog")
        @Expose
        var canBlog: Int = 0

        @SerializedName("canprint")
        @Expose
        var canPrint: Int = 0

        @SerializedName("candownload")
        @Expose
        var canDownload: Int = 0

        @SerializedName("size")
        @Expose
        lateinit var sizesList: ArrayList<SizeApi>
}