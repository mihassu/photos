package ru.mihassu.photos.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SizesResultApi {
        @SerializedName("sizes")
        @Expose
        lateinit var sizes: SizesApi
}