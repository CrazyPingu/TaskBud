package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.Folder

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFolder(folder: Folder)

    @Query("SELECT * FROM folder where userId = :userId")
    fun getAllFolder(userId: Int): List<Folder>
}