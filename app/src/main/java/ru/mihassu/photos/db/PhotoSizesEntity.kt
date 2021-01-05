package ru.mihassu.photos.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PhotoSizesEntity(

        @PrimaryKey
        val keyId: String,
        val id: Long,
        val label: String,
        val width: Int,
        val height: Int,
        val source: String,
        val url: String,
        val media: String
) { }
