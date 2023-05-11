package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mobile.todo.database.dataset.Folder

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFolder(folder: Folder)
}