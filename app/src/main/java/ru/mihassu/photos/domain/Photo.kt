package ru.mihassu.photos.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Photo(
        val id: Long,
        val title: String,
        val url: String,
        val sizes: List<PhotoSize> = listOf(),
        val comments: List<PhotoComment> = listOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val another = other as Photo
        return id == another.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "\nPhoto id: $id\ntitle: $title\n url: $url\nsizes: ${sizes.size}\n"
    }

    fun getMaxSizeUrl() : String {
        if (sizes.isNotEmpty()) {
            checkOriginalSize(sizes)
                    ?.let { original -> return original.source }
                    ?: checkLargeSize(sizes)?.let { large -> return large.source }
                    ?: let { return sizes[sizes.size - 1].source }
        } else return url
    }

    fun getLargeSizeUrl() : String {
        if (sizes.isNotEmpty()) {
            checkLargeSize(sizes)?.let { large -> return large.source }
                    ?: let { return sizes[sizes.size - 1].source }
        } else return url
    }

    private fun checkOriginalSize(sizes: List<PhotoSize>) : PhotoSize? {
        for (i in sizes) {
            if (i.label == "Original") {
                return i
            }
        }
        return null
    }

    private fun checkLargeSize(sizes: List<PhotoSize>) : PhotoSize? {
        for (i in sizes) {
            if (i.label == "Large") {
                return i
            }
        }
        return null
    }
}