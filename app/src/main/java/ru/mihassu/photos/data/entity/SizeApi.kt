package ru.mihassu.photos.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SizeApi {
        @SerializedName("label")
        @Expose
        val label: String = ""

        @SerializedName("width")
        @Expose
        val width: Int = 0

        @SerializedName("height")
        @Expose
        val height: Int = 0

        @SerializedName("source")
        @Expose
        val source: String = ""

        @SerializedName("url")
        @Expose
        val url: String = ""

        @SerializedName("media")
        @Expose
        val media: String = ""
}