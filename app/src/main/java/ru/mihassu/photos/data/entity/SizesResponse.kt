package ru.mihassu.photos.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SizesResponse {

        @SerializedName("sizes")
        @Expose
        var sizes: SizesApi? = null

        @SerializedName("stat")
        @Expose
        private val stat: String? = null
}