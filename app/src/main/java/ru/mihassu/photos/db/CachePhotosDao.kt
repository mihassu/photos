package ru.mihassu.photos.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CachePhotosDao {

    @Insert
    fun insert(photo: CachePhotosEntity)

    @Delete
    fun delete(photo: CachePhotosEntity)

    @Query("SELECT * FROM CachePhotosEntity")
    fun selectAll() : List<CachePhotosEntity>

    @Query("SELECT * FROM CachePhotosEntity WHERE id IS :photoId")
    fun selectPhotoById(photoId: Long) : List<CachePhotosEntity>

    @Query("DELETE FROM CachePhotosEntity")
    fun clear()
}