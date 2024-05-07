package com.application.stylesync.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.application.stylesync.Converters
import com.application.stylesync.MyApplication
import com.application.stylesync.Post

@Database(entities = [Post::class], version =1)
@TypeConverters(Converters::class)
abstract class LocalDBRepository: RoomDatabase() {
    abstract fun postDao(): PostDao
}

object AppLocalDatabase {

    val db: LocalDBRepository by lazy {

        val context = MyApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            LocalDBRepository::class.java,
            "LocalDB.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}