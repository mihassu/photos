package ru.mihassu.photos.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoSizesDao {

    @Insert
    fun insert(photoSize: List<PhotoSizesEntity>)

    @Delete
    fun delete(photoSize: List<PhotoSizesEntity>)

    @Query("SELECT * FROM PhotoSizesEntity WHERE id IS :photoId")
    fun selectPhotoSizesById(photoId: Long) : List<PhotoSizesEntity>


}