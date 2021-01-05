package ru.mihassu.photos.db

import androidx.room.*

@Dao
interface FavoritePhotosDao {

    @Insert
    fun insert(photo: FavoritePhotosEntity)

    @Delete
    fun delete(photo: FavoritePhotosEntity)

    @Update
    fun update(photo: FavoritePhotosEntity)

    @Query("SELECT * FROM FavoritePhotosEntity")
    fun selectAll() : List<FavoritePhotosEntity>

    @Query("SELECT * FROM FavoritePhotosEntity WHERE id IS :photoId")
    fun selectPhotoById(photoId: Long) : List<FavoritePhotosEntity>
}