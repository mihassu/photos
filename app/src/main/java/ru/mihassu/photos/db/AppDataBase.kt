package ru.mihassu.photos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoritePhotosEntity::class, CachePhotosEntity::class, PhotoSizesEntity::class], version = 4)
abstract class AppDataBase : RoomDatabase() {

    abstract fun favoritePhotosDao() : FavoritePhotosDao
    abstract fun cachePhotosDao() : CachePhotosDao
    abstract fun photoSizesDao() : PhotoSizesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context) : AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(AppDataBase::class) {
                val instance = Room.databaseBuilder(context, AppDataBase::class.java, "photosDb1")
                        .addMigrations(MIGRATION_4_TO_5)
                        .addMigrations(MIGRATION_5_TO_4)
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

val MIGRATION_4_TO_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE PhotoSizesEntity")
        database.execSQL("CREATE TABLE PhotoSizesEntity(keyId TEXT NOT NULL PRIMARY KEY, id BIGINT NOT NULL, label TEXT NOT NULL, width INTEGER NOT NULL, height INTEGER NOT NULL, source TEXT NOT NULL, url TEXT NOT NULL, media TEXT NOT NULL)")

    }
}

val MIGRATION_5_TO_4 = object : Migration(5, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE PhotoSizesEntity")
        database.execSQL("CREATE TABLE PhotoSizesEntity(keyId TEXT NOT NULL PRIMARY KEY, id BIGINT NOT NULL, label TEXT NOT NULL, width INTEGER NOT NULL, height INTEGER NOT NULL, source TEXT NOT NULL, url TEXT NOT NULL, media TEXT NOT NULL)")
    }
}
