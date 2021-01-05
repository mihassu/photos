package ru.mihassu.photos.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CachePhotosEntity(@PrimaryKey val id: Long, val title: String, val url: String) {
}