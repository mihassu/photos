package ru.mihassu.photos.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PhotoInfoEntity(

        @PrimaryKey
        val keyId: String,
        val photoId: Long,
        val label: String,
        val width: Int,
        val height: Int,
        val source: String,
        val url: String,
        val media: String
) { }
