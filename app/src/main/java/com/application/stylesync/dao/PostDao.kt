package com.application.stylesync.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.application.stylesync.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getAll(): LiveData<MutableList<Post>>

    @Query("SELECT * FROM post WHERE userId = :userId")
    fun getByUserId(userId: String): LiveData<MutableList<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(post: Post)

    @Delete
    fun delete(post: Post)

}