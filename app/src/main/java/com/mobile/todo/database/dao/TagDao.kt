package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTag(tag: Tag)

    @Query("SELECT * FROM tag")
    fun getAllTag(): List<Tag>

    @Query("SELECT * FROM tag, todo WHERE userId = :userId AND tag = :favorite LIMIT 1;")
    fun usedFavouriteTag(userId: Int, favorite : String = Tag.FAV): Boolean
}